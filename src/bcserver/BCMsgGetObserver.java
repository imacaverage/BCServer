/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCGame;
import bc.BCLog;
import bc.BCMsgGet;
import bc.BCMsgSend;
import bc.BCPlayer;
import bc.BCRoom;
import bc.BCTable;
import java.util.Observable;
import java.util.Observer;

/**
 * Класс "Наблюдатель за объектом 'Прием сообщений'"
 * @author iMacAverage
 */
public class BCMsgGetObserver implements Observer {
        
    /**
     * объект "Сервер"
     */    
    private final BCServer bcServer;
    
    /**
     * объект "Игровая комната"
     */
    private final BCRoom bcRoom;
    
    /**
     * объект "База данных"
     */
    private final BCDataBase bcDataBase;
    
    /**
     * объект "Отправка сообщений"
     */
    private final BCMsgSend bcMsgSend;
    
    /**
     * объект "Лог"
     */
    private final BCLog bcLog;
    
    /**
     * объект "Игрок"
     */
    private BCPlayer bcPlayer;
    
    /**
     * объект "Стол"
     */
    private BCTable bcTable;

    /**
     * объект "Игра"
     */
    private BCGame bcGame;

    /**
     * ip адрес клиента
     */
    private final String ipAddress;

    /**
     * интерфейс "Состояние объекта 'Наблюдатель объекта Прием сообщений'"
     */
    private IBCMsgGetObserverState iBCMsgGetObserverState;

    /**
     * Создать объект
     * @param bcServer объект "Сервер"
     * @param bcRoom объект "Игровая комната"
     * @param bcDataBase объект "База данных"
     * @param bcMsgSend объект "Отправка сообщений"
     * @param bcLog объект "Лог"
     * @param ipAddress ip адрес клиента
     */
    public BCMsgGetObserver(BCServer bcServer, BCRoom bcRoom, BCDataBase bcDataBase, BCMsgSend bcMsgSend, BCLog bcLog, String ipAddress) {
        this.bcServer = bcServer;
        this.bcRoom = bcRoom;
        this.bcDataBase = bcDataBase;
        this.bcMsgSend = bcMsgSend;
        this.bcLog = bcLog;
        this.ipAddress = ipAddress;
        this.bcPlayer = null;
        this.bcTable = null;
        this.bcGame = null;
        this.iBCMsgGetObserverState = new BCMsgGetObserverStateStart(this.bcServer.getVersion());
    }
    
    /**
     * Получить объект "Сервер"
     * @return объект "Сервер"
     */
    public BCServer getBCServer() {
        return this.bcServer;
    }
    
    /**
     * Получить объект "Игровая комната"
     * @return объект "Игровая комната"
     */
    public BCRoom getBCRoom() {
        return this.bcRoom;
    }
    
    /**
     * Получить объект "База данных"
     * @return объект "База данных"
     */
    public BCDataBase getBCDataBase() {
        return this.bcDataBase;
    }
    
    /**
     * Получить объект "Отправка сообщений"
     * @return объект "Отправка сообщений"
     */
    public BCMsgSend bcMsgSend() {
        return this.bcMsgSend;
    }
    
    /**
     * Задать объект "Игрок"
     * @param bcPlayer объект "Игрок"
     */
    public void setBCPlayer(BCPlayer bcPlayer) {
        this.bcPlayer = bcPlayer;
    }
    
    /**
     * Получить объект "Игрок"
     * @return объект "Игрок"
     */
    public BCPlayer getBCPlayer() {
        return this.bcPlayer;
    }
    
    /**
     * Задать объект "Стол"
     * @param bcTable объект "Стол"
     */
    public void setBCTable(BCTable bcTable) {
        this.bcTable = bcTable;
    }
    
    /**
     * Получить объект "Стол"
     * @return объект "Стол"
     */
    public BCTable getBCTable() {
        return this.bcTable;
    }

    /**
     * Задать объект "Игра"
     * @param bcGame объект "Игра"
     */
    public void setBCGame(BCGame bcGame) {
        this.bcGame = bcGame;
    }
    
    /**
     * Получить объект "Игра"
     * @return объект "Игра"
     */
    public BCGame getBCGame() {
        return this.bcGame;
    }

    /**
     * Получить ip адрес клиента
     * @return ip адрес клиента
     */
    public String getIPAddress() {
        return this.ipAddress;
    }
    
    /**
     * Получить объект "Лог"
     * @return объект "Лог"
     */
    public BCLog getBCLog() {
        return this.bcLog;
    }
    
    /**
     * Задать интерфейс "Состояние объекта 'Наблюдатель объекта Прием сообщений'"
     * @param iBCMsgGetObserverState интерфейс "Состояние объекта 'Наблюдатель объекта Прием сообщений'"
     */
    public void setIBCMsgGetObserverState(IBCMsgGetObserverState iBCMsgGetObserverState) {
        this.iBCMsgGetObserverState = iBCMsgGetObserverState;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        iBCMsgGetObserverState.processBCMsg(this, (BCMsgGet) o);
    }
    
}
