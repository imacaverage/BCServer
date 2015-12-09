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
import bc.BCMsgNewTableCancel;
import bc.BCMsgSend;
import bc.BCMsgSitTableKick;
import bc.BCMsgSitTableOut;
import bc.BCPlayer;
import bc.BCRoom;
import bc.BCTable;

/**
 * Класс "Состояние: За столом: игрок Б"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Уйти со стола"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Выгнать со стола"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Игра: запрос"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Игра: ответ"
 * @author iMacAverage
 */
public class BCMsgGetObserverStateAtTableB implements IBCMsgGetObserverState {

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
            // удалить игрока Б со стола
            BCTable bcTable = bcRoom.findTable(bcMsgGetObserver.getBCTable().getNumTable());
            BCPlayer bcPlayerA = bcTable.getBCPlayerA();
            bcTable.setBCPlayerB(null);
            // отправить игроку А объект "Сообщение: Уйти со стола"
            BCMsgSitTableOut bcMsgSitTableOut = new BCMsgSitTableOut(bcTable);
            bcPlayerA.sendBCMsg(bcMsgSitTableOut);
            // удалить игрока Б из игровой комнаты
            bcRoom.removePlayer(bcMsgGetObserver.getBCPlayer().getLogin());
            return;
        }
        
        // если пришло "Сообщение: Игра: запрос"
        BCMsgGameRequest bcMsgGameRequest = (BCMsgGameRequest) bcMsgGet.getBCMsg("BCMsgGameRequest");
        if(bcMsgGameRequest != null) {
            BCTable bcTable = bcRoom.findTable(bcMsgGameRequest.getBCTable().getNumTable());
            // если стол уже был удален
            if(bcTable == null)
                return;
            BCPlayer bcPlayerA = bcTable.getBCPlayerA();
            bcPlayerA.sendBCMsg(bcMsgGameRequest);
            return;
        }
        
        // если пришло "Сообщение: Игра: ответ"
        BCMsgGameResponse bcMsgGameResponse = (BCMsgGameResponse) bcMsgGet.getBCMsg("BCMsgGameResponse");
        if(bcMsgGameResponse != null) {
            BCTable bcTable = bcRoom.findTable(bcMsgGameResponse.getBCTable().getNumTable());
            // замена объекта состояние на объект "Состояние: Игра: игрок Б"
            BCGame bcGame = bcMsgGetObserver.getBCServer().getBCGame(bcTable.getNumTable());
            bcMsgGetObserver.setBCGame(bcGame);
            BCMsgGetObserverStateGameB bcMsgGetObserverStateGameB = new BCMsgGetObserverStateGameB(bcMsgGetObserver);
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateGameB);
            bcGame.addObserver(bcMsgGetObserverStateGameB);
            return;
        }

        //если пришло "Сообщение: Уйти со стола"
        BCMsgSitTableOut bcMsgSitTableOut = (BCMsgSitTableOut) bcMsgGet.getBCMsg("BCMsgSitTableOut");
        if(bcMsgSitTableOut != null) {
            BCTable bcTable = bcRoom.findTable(bcMsgSitTableOut.getBCTable().getNumTable());
            // если стол уже был удален
            if(bcTable == null)
                return;
            // удалить игрока Б из за стола
            bcTable.setBCPlayerB(null);
            // разослать всем игрокам изменное состояние игровой комнаты
            bcRoom.setChange();
            // отправить сообщение игроку А
            BCPlayer bcPlayerA = bcTable.getBCPlayerA();
            bcPlayerA.sendBCMsg(bcMsgSitTableOut);
            // замена объекта состояние объекта наблюдатель за объектом прием сообщений (в игровой комнате)
            BCMsgGetObserverStateInRoom bcMsgGetObserverStateInRoom = new BCMsgGetObserverStateInRoom();
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateInRoom);
            return;
        }

        //если пришло сообщение Отмена создания стола
        BCMsgNewTableCancel bcMsgNewTableCancel = (BCMsgNewTableCancel) bcMsgGet.getBCMsg("BCMsgNewTableCancel");
        if(bcMsgNewTableCancel != null) {
            // замена объекта состояние объекта наблюдатель за объектом прием сообщений (в игровой комнате)
            BCMsgGetObserverStateInRoom bcMsgGetObserverStateInRoom = new BCMsgGetObserverStateInRoom();
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateInRoom);
            return;
        }

        //если пришло сообщение Выгнать со стола
        BCMsgSitTableKick bcMsgSitTableKick = (BCMsgSitTableKick) bcMsgGet.getBCMsg("BCMsgSitTableKick");
        if(bcMsgSitTableKick != null) {
            // замена объекта состояние объекта наблюдатель за объектом прием сообщений (в игровой комнате)
            BCMsgGetObserverStateInRoom bcMsgGetObserverStateInRoom = new BCMsgGetObserverStateInRoom();
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateInRoom);
        }
    
    }
    
}
