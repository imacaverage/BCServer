/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCLog;
import bc.BCMsgError;
import bc.BCMsgGet;
import bc.BCMsgSend;
import bc.BCMsgNewTableRequest;
import bc.BCMsgNewTableResponse;
import bc.BCMsgSitTableRequest;
import bc.BCMsgSitTableResponse;
import bc.BCPlayer;
import bc.BCRoom;
import bc.BCTable;

/**
 * Класс "Состояние: В игровой комнате"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Создать стол: запрос"
 * Наблюдатель объекта "Прием сообщений": "Сообщение: Сесть за стол: запрос"
 * @author iMacAverage
 */
public class BCMsgGetObserverStateInRoom implements IBCMsgGetObserverState {
    
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
            bcRoom.removePlayer(bcMsgGetObserver.getBCPlayer().getLogin());
            return;
        }

        // если пришел запрос на создание стола
        BCMsgNewTableRequest bcMsgNewTableRequest = (BCMsgNewTableRequest) bcMsgGet.getBCMsg("BCMsgNewTableRequest");
        if(bcMsgNewTableRequest != null) {
            BCTable bcTableReq = bcMsgNewTableRequest.getBCTable();
            BCPlayer bcPlayer = new BCPlayer(bcTableReq.getBCPlayerA().getLogin(), bcTableReq.getBCPlayerA().getRating(), bcMsgSend);
            BCTable bcTable = new BCTable(bcRoom.getNumTable(), bcPlayer, bcTableReq.getMinRating(), bcTableReq.getNumberLength(), bcTableReq.getWithRepeat(), bcTableReq.getCountMin());
            BCMsgNewTableResponse bcMsgNewTableResponse = new BCMsgNewTableResponse(bcTable.getNumTable());
            // в случае успешной отправки сообщения о создании нового стола
            if(bcMsgSend.sendBCMsg(bcMsgNewTableResponse)) {
                bcRoom.addTable(bcTable);
                bcMsgGetObserver.setBCTable(bcTable);
                // замена объекта состояние объекта наблюдатель за объектом прием сообщений (за столом, игрок А)
                BCMsgGetObserverStateAtTableA bcMsgGetObserverStateAtTableA = new BCMsgGetObserverStateAtTableA();
                bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateAtTableA);                
            }
            return;
        }        
        
        // если пришел запрос сесть за стол
        BCMsgSitTableRequest bcMsgSitTableRequest = (BCMsgSitTableRequest) bcMsgGet.getBCMsg("BCMsgSitTableRequest");
        if(bcMsgSitTableRequest != null) {
            BCPlayer bcPlayerB = bcRoom.findPlayer(bcMsgSitTableRequest.getBCPlayer().getLogin());
            // если игрок Б не найден
            if(bcPlayerB == null)
                return;
            BCTable bcTable = bcRoom.findTable(bcMsgSitTableRequest.getBCTable().getNumTable());
            // если стол не найден
            if(bcTable == null) {
                BCMsgSitTableResponse bcMsgSitTableResponse = new BCMsgSitTableResponse(bcPlayerB, bcTable, "Table not found!");
                bcMsgSend.sendBCMsg(bcMsgSitTableResponse);
                return;
            } 
            BCPlayer bcPlayerA = bcTable.getBCPlayerA();
            // если игрок А не найден
            if(bcPlayerA == null)
                return;
            // если место уже занято
            if(bcTable.getBCPlayerB() != null) {
                BCMsgSitTableResponse bcMsgSitTableResponse = new BCMsgSitTableResponse(bcPlayerB, bcTable, "Table busy!");
                bcMsgSend.sendBCMsg(bcMsgSitTableResponse);
                return;
            }
            // если игрок Б забанен на данном столе
            if(bcTable.checkBanned(bcPlayerB)) {
                BCMsgSitTableResponse bcMsgSitTableResponse = new BCMsgSitTableResponse(bcPlayerB, bcTable, "You are banned at table!");
                bcMsgSend.sendBCMsg(bcMsgSitTableResponse);
                return;
            }
            // добавить игрока Б за стол
            bcTable.setBCPlayerB(bcPlayerB);
            // разослать всем игрокам изменное состояние игровой комнаты
            bcRoom.setChange();
            // отправить сообщение об успешном присоединении к столу игроку Б
            BCMsgSitTableResponse bcMsgSitTableResponse = new BCMsgSitTableResponse(bcPlayerB, bcTable);
            if(bcMsgSend.sendBCMsg(bcMsgSitTableResponse)) {
                bcMsgGetObserver.setBCTable(bcTable);
                // отправить сообщение об успешном присоединении к столу игроку А
                bcPlayerA.sendBCMsg(bcMsgSitTableResponse);        
                // замена объекта состояние объекта наблюдатель за объектом прием сообщений (за столом, игрок Б)
                BCMsgGetObserverStateAtTableB bcMsgGetObserverStateAtTableB = new BCMsgGetObserverStateAtTableB();
                bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateAtTableB);
            }

        }

    }
    
}
