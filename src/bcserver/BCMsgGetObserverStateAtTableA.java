/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCGame;
import bc.BCLog;
import bc.BCMsgError;
import bc.BCMsgGameRequest;
import bc.BCMsgGameResponse;
import bc.BCMsgGet;
import bc.BCMsgSend;
import bc.BCMsgNewTableCancel;
import bc.BCMsgSitTableKick;
import bc.BCPlayer;
import bc.BCRoom;
import bc.BCTable;
import java.util.Timer;

/**
 * Класс "Состояние: За столом: игрок А"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Отмена создания стола"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Выгнать со стола"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Игра: запрос"
 * @author iMacAverage
 */
public class BCMsgGetObserverStateAtTableA implements IBCMsgGetObserverState {

    @Override
    public void processBCMsg(BCMsgGetObserver bcMsgGetObserver, BCMsgGet bcMsgGet) {

        BCLog bcLog = bcMsgGetObserver.getBCLog();
        BCRoom bcRoom = bcMsgGetObserver.getBCRoom();
        BCMsgSend bcMsgSend = bcMsgGetObserver.bcMsgSend();
        String ipAddress = bcMsgGetObserver.getIPAddress();
        String login = bcMsgGetObserver.getBCPlayer().getLogin();

        // если процесс приема сообщений остановился
        BCMsgError bcMsgError = (BCMsgError) bcMsgGet.getBCMsg("BCMsgError");
        
        if(bcMsgError != null) {
            
            bcLog.logWrite(ipAddress + ": login: " + login + " error: " + bcMsgError.getError());
            bcMsgGet.deleteObserver(bcMsgGetObserver);
            BCTable bcTable = bcRoom.findTable(bcMsgGetObserver.getBCTable().getNumTable());
            BCPlayer bcPlayerA = bcTable.getBCPlayerA();
            BCPlayer bcPlayerB = bcTable.getBCPlayerB();
            
            // отправить игроку Б объект "Сообщение: Отмена создания стола"
            if(bcPlayerB != null) {
                BCMsgNewTableCancel bcMsgNewTableCancel = new BCMsgNewTableCancel(bcTable);
                bcPlayerB.sendBCMsg(bcMsgNewTableCancel);
            }
            
            // удалить стол из игровой комнаты
            bcRoom.removeTable(bcTable.getNumTable());
            
            // удалить игрока А из игровой комнаты
            bcRoom.removePlayer(bcPlayerA.getLogin());
            return;
        
        }
        
        // если пришло "Сообщение: Игра: запрос"
        BCMsgGameRequest bcMsgGameRequest = (BCMsgGameRequest) bcMsgGet.getBCMsg("BCMsgGameRequest");
        
        if(bcMsgGameRequest != null) {
            
            BCTable bcTable = bcRoom.findTable(bcMsgGameRequest.getBCTable().getNumTable());
            BCPlayer bcPlayerB = bcTable.getBCPlayerB();
            
            // если игрока Б уже нет
            if(bcPlayerB == null)
                return;
            
            // создание игры
            BCGame bcGame = new BCGame(bcTable, bcMsgGameRequest.getNumberA(), bcMsgGameRequest.getNumberB());
            bcMsgGetObserver.setBCGame(bcGame);
            bcMsgGetObserver.getBCServer().addBCGame(bcGame);

            // отправляю игроку Б объект "Сообщение: Игра: ответ"
            BCMsgGameResponse bcMsgGameResponse = new BCMsgGameResponse(bcTable, bcMsgGameRequest.getNumberA(), bcMsgGameRequest.getNumberB());
            bcPlayerB.sendBCMsg(bcMsgGameResponse);
            bcMsgSend.sendBCMsg(bcMsgGameResponse);
                        
            // запуск таймера для отправки времени игрокам
            BCTimerGame bcTimerGame = new BCTimerGame(bcGame);
            bcGame.addObserver(bcTimerGame);
            Timer timer = new Timer();
            timer.schedule(bcTimerGame, 0, 500);
            
            // замена объекта состояние на объект "Состояние: Игра: игрок А"
            BCMsgGetObserverStateGameA bcMsgGetObserverStateGameA = new BCMsgGetObserverStateGameA(bcMsgGetObserver);
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateGameA);
            bcGame.addObserver(bcMsgGetObserverStateGameA);
            return;
        
        }
        
        // если пришло "Сообщение: Отмена создания стола"
        BCMsgNewTableCancel bcMsgNewTableCancel = (BCMsgNewTableCancel) bcMsgGet.getBCMsg("BCMsgNewTableCancel");
        
        if(bcMsgNewTableCancel != null) {
            
            BCTable bcTable = bcRoom.findTable(bcMsgNewTableCancel.getBCTable().getNumTable());
            BCPlayer bcPlayerB = bcTable.getBCPlayerB();
            
            // если за столом был игрок Б
            if(bcPlayerB != null)
                bcPlayerB.sendBCMsg(bcMsgNewTableCancel);
            
            bcRoom.removeTable(bcTable.getNumTable());
            
            // замена объекта состояние объекта наблюдатель за объектом прием сообщений (в игровой комнате)
            BCMsgGetObserverStateInRoom bcMsgGetObserverStateInRoom = new BCMsgGetObserverStateInRoom();
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateInRoom);
            return;
        
        } 

        //если пришло "Сообщение: Выгнать со стола"
        BCMsgSitTableKick bcMsgSitTableKick = (BCMsgSitTableKick) bcMsgGet.getBCMsg("BCMsgSitTableKick");
        
        if(bcMsgSitTableKick != null) {
            
            BCTable bcTable = bcRoom.findTable(bcMsgSitTableKick.getBCTable().getNumTable());
            BCPlayer bcPlayerB = bcTable.getBCPlayerB();
        
            bcPlayerB.sendBCMsg(bcMsgSitTableKick);
            
            // удалить игрока Б из за стола
            bcTable.kickBCPlayerB();
            
            // разослать всем игрокам изменное состояние игровой комнаты
            bcRoom.setChange();
        
        }
            
    }
    
}
