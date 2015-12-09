/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCGame;
import bc.BCLog;
import bc.BCMsgGet;
import bc.BCMsgGetObserverPingRequest;
import bc.BCMsgSend;
import bc.BCPlayer;
import bc.BCRoom;
import bc.BCRoomObserver;
import bc.BCTimerPing;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Timer;

/**
 * Класс "Сервер"
 * @author iMacAverage
 */
public class BCServer {

    /**
     * версия клиента
     */
    private final int VERSION = 18;
    
    /** 
     * порт сервера 
     */
    private final static int PORT = 4444;
    
    /** 
     * задержка для пинга 
     */
    private final static int PING_DELAY = 3000;

    /**
     * объект "Лог"
     */
    private final BCLog bcLog;
    
    /** 
     * объект "Игровая комната"
     */
    private final BCRoom bcRoom;
           
    /** 
     * объект "Наблюдатель объекта 'Игровая комната'" 
     */
    private final BCRoomObserver bcRoomObserver;
    
    /**
     * коллекция объектов "Игра"
     */
    private final ArrayList<BCGame> bcGames;
    
    /**
     * строка соединения с базой данных
     */
    private final String dbConn;
    
    /**
     * логин для соединения с базой данных
     */
    private final String dbLogin;
    
    /**
     * пароль для соединения с базой данных
     */
    private final String dbPassword;


    /**
     * Создать объект
     * @param iniFileName имя файла настроек
     */
    public BCServer(String iniFileName) {
        
        FileWriter logFile = null;
        
        this.bcGames = new ArrayList<>();
        this.bcRoom = new BCRoom();
        this.bcRoomObserver = new BCRoomObserver();
        this.bcRoom.addObserver(bcRoomObserver);
        
        // открыть файл настроек
        Properties ini = new Properties();
        try {        
            ini.load(new FileInputStream(new File(iniFileName)));
        } 
        catch (IOException ex) {
            System.exit(1);
        }
        
        // получить имя лог файла
        String logFileName = ini.getProperty("Log", "BCServer.log");
        try {
            logFile = new FileWriter(logFileName);
        } 
        catch (IOException ex) {
            System.exit(1);
        }
        
        this.bcLog = new BCLog(logFile);
        
        // получить параметры для соединения с базой данных
        this.dbConn = ini.getProperty("Conn", "jdbc:mysql://localhost:3306/bc");
        this.dbLogin = ini.getProperty("Login", "root");
        this.dbPassword = ini.getProperty("Password", "ghjcnjnfr");
            
    }
    
    /**
     * Получить версию клиента
     * @return версия клиента
     */
    public int getVersion() {
        return this.VERSION;
    }
    
    /**
     * Добавить объект "Игра" в коллекцию
     * @param bcGame объект "Игра"
     */
    public synchronized void addBCGame(BCGame bcGame) {
        this.bcGames.add(bcGame);
    }
    
    /**
     * Получить объект "Игра"
     * @param numTable номер стола
     * @return объект "Игра"
     */
    public synchronized BCGame getBCGame(int numTable) {
        for(BCGame bcGame : this.bcGames)
            if(bcGame.getBCTable().getNumTable() == numTable)
                return bcGame;
        return null;
    }
    
    /**
     * Удалить игру
     * @param numTable номер стола
     */
    public synchronized void removeBCGame(int numTable) {
        for(Iterator<BCGame> it = this.bcGames.iterator(); it.hasNext();) {
            BCGame bcGame = it.next();
            if(bcGame.getBCTable().getNumTable() == numTable)
                it.remove();
        }
    }
    
    /**
     * Обновить рейтинги игроков
     * @param bcGame объект "Игра"
     * @param bcDataBase объект "База данных"
     */
    public void updateRating(BCGame bcGame, BCDataBase bcDataBase) {
        BCPlayer bcPlayerA = this.bcRoom.findPlayer(bcGame.getBCTable().getBCPlayerA().getLogin());
        BCPlayer bcPlayerB = this.bcRoom.findPlayer(bcGame.getBCTable().getBCPlayerB().getLogin());
        double oldRatingA = bcPlayerA.getRating();
        double oldRatingB = bcPlayerB.getRating();
        double newRatingA, newRatingB; 
        double scoreA, scoreB;
        switch(bcGame.getState()) {
            case WON:
                scoreA = 1;
                break;
            case WON_TIME:    
                scoreA = 1;
                break;
            case WON_LOST:    
                scoreA = 1;
                break;
            case LOST:
                scoreA = 0;
                break;
            case LOST_TIME:    
                scoreA = 0;
                break;
            case LOST_LOST:    
                scoreA = 0;
                break;
            case DRAW:
                scoreA = 0.5;
                break;
            default: 
                scoreA = 0.5;
                break;
        }
        scoreB = 1 - scoreA;
        newRatingA = this.calcRatingA(oldRatingA, oldRatingB, scoreA);
        newRatingB = this.calcRatingA(oldRatingB, oldRatingA, scoreB);
        try {
            bcDataBase.setRating(bcPlayerA.getLogin(), (int) newRatingA);
            bcDataBase.setRating(bcPlayerB.getLogin(), (int) newRatingB);
        } 
        catch (SQLException ex) {
            this.bcLog.logWrite("Error update rating\n");
            return;
        }
        bcPlayerA.setRating((int) newRatingA);
        bcPlayerB.setRating((int) newRatingB);
        this.bcRoom.setChange();
    }
    
    /**
     * Вычислить рейтинг игрока А
     * @param ratingA исходный рейтинг игрока А
     * @param ratingB исходный рейтинг игрока Б
     * @param scoreA набранные очки игроком А (1 - победа, 0.5 - ничья, 0 - поражение)
     * @return рейтинг игрока А
     */
    private double calcRatingA(double ratingA, double ratingB, double scoreA) {
        double e = 1 / ( 1 + (Math.pow(10, (ratingB - ratingA) / 400)) );
        return ( ratingA + 15 * (scoreA - e) );
    }
    
    /**
     * Обработать подключение клиента
     */
    private void processClientConnect(Socket socket) {
    
        BCDataBase bcDataBase = null;
        String ip = socket.getInetAddress().getHostAddress();
        ObjectInputStream inStream;
        ObjectOutputStream outStream;
        
        // создать потоки для обмена объектами сообщений
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream   = new ObjectInputStream(socket.getInputStream());
        } 
        catch (IOException ex) {
            this.bcLog.logWrite(ip + " - " + "Error creating thread messaging client\n");
            return;
        }

        try {
            bcDataBase = new BCDataBase(this.dbConn, this.dbLogin, this.dbPassword);
        } 
        catch (SQLException ex) {
            this.bcLog.logWrite("Error connecting to the database\n");
            System.exit(1);
        }

        // объект получения сообщений
        BCMsgGet bcMsgGet = new BCMsgGet(inStream);

        // объект отправка сообщений
        BCMsgSend bcMsgSend = new BCMsgSend(outStream);

        // объект наблюдатель объекта прием сообщений (сообщение пинг (запрос))
        BCMsgGetObserverPingRequest bcMsgGetObserverPingRequest = new BCMsgGetObserverPingRequest(bcMsgSend);
        bcMsgGet.addObserver(bcMsgGetObserverPingRequest);

        // объект наблюдатель объекта прием сообщений
        BCMsgGetObserver bcMsgGetObserver = new BCMsgGetObserver(this, this.bcRoom, bcDataBase, bcMsgSend, this.bcLog, socket.getInetAddress().getHostAddress());
        bcMsgGet.addObserver(bcMsgGetObserver);

        // объект наблюдатель объекта прием сообщений (только объект пинг-ответ)
        BCTimerPing bcTimerPing = new BCTimerPing(bcMsgGet, bcMsgSend);
        bcMsgGet.addObserver(bcTimerPing);

        // запуск объекта прием сообщений в отдельном потоке
        Thread thread = new Thread(bcMsgGet);
        thread.start();

        // запуск объекта пинг на таймере
        Timer timer = new Timer();
        timer.schedule(bcTimerPing, 0, BCServer.PING_DELAY);
    
    }
        
    /**
     * Запустить сервер
     */
    public void run() {

        Socket socket = null;
        ServerSocket serverSocket = null;
        
        this.bcLog.logWrite("Server start\n");
        
        try{
            serverSocket = new ServerSocket(BCServer.PORT);
        }
        catch (IOException ex) {
            this.bcLog.logWrite("Error creating socket, server is down\n");
            System.exit(1);
	}        
        
        this.bcLog.logWrite("Waiting for client connection\n");
        
        // слушать порт
        while(true) {
            try {
                socket = serverSocket.accept();
            }
            catch (IOException ex) {
                this.bcLog.logWrite("Error creating thread to connect the client, server is down\n");
                this.bcLog.logClose();
                System.exit(1);
            }
            // Обработать подключение клиента
            this.processClientConnect(socket);
        }        
    
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {   
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String path = "";
                if(args.length > 0)
                    path += args[0];
                BCServer bcServer = new BCServer(path + "BCServer.ini");
                bcServer.run();
            }
        });
    }
    
}
