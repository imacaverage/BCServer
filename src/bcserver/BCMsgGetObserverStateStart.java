/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCLog;
import bc.BCMsgError;
import bc.BCMsgGet;
import bc.BCMsgRegRequest;
import bc.BCMsgRegResponse;
import bc.BCMsgSend;
import bc.BCPlayer;
import bc.BCRoom;
import java.sql.SQLException;

/**
 * Класс "Состояние: Запущен"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Логирование: запрос"
 * @author iMacAverage
 */
public class BCMsgGetObserverStateStart implements IBCMsgGetObserverState {

    /**
     * версия клиента
     */
    private final int version;
    
    /**
     * Создать объект
     * @param version версия клиента 
     */
    public BCMsgGetObserverStateStart(int version) {
        this.version = version;
    }
    
    /**
     * Получить объект ответа по регистрации
     * @param bcMsgRegRequest объект сообщение логирование (запрос)
     * @param bcRoom объект игровая комната
     * @param bcDataBase объект база данных
     * @param bcLog объект лог
     * @return объект сообщение логирование (ответ)
     */
    private BCMsgRegResponse getBCMsgRegResponse(BCMsgRegRequest bcMsgRegRequest, BCRoom bcRoom, BCDataBase bcDataBase, BCLog bcLog, String ipAddress, int version) {
        int rating;
        boolean findPlayer, checkPlayer;
        boolean newPlayer = bcMsgRegRequest.getNewPlayer();
        String login = bcMsgRegRequest.getLogin();
        String password = bcMsgRegRequest.getPassword();
        if(bcRoom.findPlayer(login) != null) {
            bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Player is now on a server\n");
            return new BCMsgRegResponse(login, "Player " + login + " is now on a server!");
        }
        try {
            findPlayer = bcDataBase.findPlayer(login);
        } 
        catch (SQLException ex) {
            bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Error search player table players\n");
            return new BCMsgRegResponse(login, "Error search player!");
        }
        if(!findPlayer && !newPlayer) {
            bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "The player is not found\n");
            return new BCMsgRegResponse(login, "The player is not found!");
        }
        if(findPlayer && newPlayer) {
            bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "The player already exists\n");
            return new BCMsgRegResponse(login, "The player already exists!");
        }
        // добавление нового игрока 
        if(!findPlayer && newPlayer) {
            try {
                bcDataBase.addPlayer(login, password);
            } 
            catch (SQLException ex) {
                bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Error create player\n");
                return new BCMsgRegResponse(login, "Error create player!");
            }
            try {
                rating = bcDataBase.getRating(login);
            } 
            catch (SQLException ex) {
                bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Error get data of player\n");
                return new BCMsgRegResponse(login, "Error get data of player!");
            }
            bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Successful registartion new player\n");
            return new BCMsgRegResponse(login, rating, version);
        }
        // проверка пароля
        if(findPlayer && !newPlayer) {
            try {
                checkPlayer = bcDataBase.checkPassword(login, password);
            } 
            catch (SQLException ex) {
                bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Error checking password\n");
                return new BCMsgRegResponse(login, "Error checking password!");
            }
            if(checkPlayer) {
                try {
                    rating = bcDataBase.getRating(login);
                } 
                catch (SQLException ex) {
                    bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Error get data of player\n");
                    return new BCMsgRegResponse(login, "Error get data of player!");
                }
                bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Successful registartion\n");
                return new BCMsgRegResponse(login, rating, version);
            }
            else {
                bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Wrong password\n");
                return new BCMsgRegResponse(login, "Wrong password!");
            }
        }
        bcLog.logWrite(ipAddress + ": " + "Registration, login: " + login + " - " + "Error registration player\n");
        return new BCMsgRegResponse(login, "Error registration player!");
    }
    
    @Override
    public void processBCMsg(BCMsgGetObserver bcMsgGetObserver, BCMsgGet bcMsgGet) {
        
        BCLog bcLog = bcMsgGetObserver.getBCLog();
        BCRoom bcRoom = bcMsgGetObserver.getBCRoom();
        BCMsgSend bcMsgSend = bcMsgGetObserver.bcMsgSend();
        BCDataBase bcDataBase = bcMsgGetObserver.getBCDataBase();
        String ipAddress = bcMsgGetObserver.getIPAddress();

        // если процесс приема сообщений остановился
        BCMsgError bcMsgError = (BCMsgError) bcMsgGet.getBCMsg("BCMsgError");
        if(bcMsgError != null) {
            bcLog.logWrite(ipAddress + ": " + bcMsgError.getError());
            bcMsgGet.deleteObserver(bcMsgGetObserver);
            return;
        }

        // получить объект сообщение логирования (запрос)
        BCMsgRegRequest bcMsgRegRequest = (BCMsgRegRequest) bcMsgGet.getBCMsg("BCMsgRegRequest");
        if(bcMsgRegRequest == null)
            return;

        // получить объект ответа по регистрацииПолучить объект ответа по регистрации
        BCMsgRegResponse bcMsgRegResponse = this.getBCMsgRegResponse(bcMsgRegRequest, bcRoom, bcDataBase, bcLog, ipAddress, this.version);
        
        // в случае успешной регистрации создается объект игрок и добавляется в игровую комнату
        if(bcMsgSend.sendBCMsg(bcMsgRegResponse) && bcMsgRegResponse.getRegistration()) {
            BCPlayer bcPlayer = new BCPlayer(bcMsgRegRequest.getLogin(), bcMsgRegResponse.getRating(), bcMsgSend);            
            bcRoom.addPlayer(bcPlayer);
            bcMsgGetObserver.setBCPlayer(bcPlayer);
            // замена объекта состояние объекта наблюдатель за объектом прием сообщений (в игровой комнате)
            BCMsgGetObserverStateInRoom bcMsgGetObserverStateInRoom = new BCMsgGetObserverStateInRoom();
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateInRoom);
        }
    
    }
    
}
