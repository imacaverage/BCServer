/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCGame;
import bc.BCGameState;
import bc.BCLog;
import bc.BCMsgError;
import bc.BCMsgGet;
import bc.BCMsgLost;
import bc.BCMsgSend;
import bc.BCMsgSitTableOut;
import bc.BCMsgTurn;
import bc.BCPlayer;
import bc.BCRoom;
import bc.BCTable;
import java.util.Observable;
import java.util.Observer;

/**
 * Класс "Состояние: Игра: игрок Б"
 * Обрабатывает сообщения:
 * Сообщение: Ошибка
 * Сообщение: Сдаться
 * Сообщение: Ход
 * Наблюдатель объекта "Игра"
 * @author iMacAverage
 */
public class BCMsgGetObserverStateGameB implements IBCMsgGetObserverState, Observer {
    
    /**
     * объект "Наблюдатель за объектом 'Прием сообщений'"
     */
    private final BCMsgGetObserver bcMsgGetObserver;
    
    /**
     * Создать объект 
     * @param bcMsgGetObserver объект "Наблюдатель за объектом 'Прием сообщений'"
     */
    public BCMsgGetObserverStateGameB(BCMsgGetObserver bcMsgGetObserver) {
        this.bcMsgGetObserver = bcMsgGetObserver;
    }

    @Override
    public void processBCMsg(BCMsgGetObserver bcMsgGetObserver, BCMsgGet bcMsgGet) {

        BCLog bcLog = bcMsgGetObserver.getBCLog();
        BCRoom bcRoom = bcMsgGetObserver.getBCRoom();
        BCPlayer bcPlayer = bcMsgGetObserver.getBCPlayer();
        BCTable bcTable = bcMsgGetObserver.getBCTable();
        BCGame bcGame = bcMsgGetObserver.getBCGame();
        BCMsgSend bcMsgSend = bcMsgGetObserver.bcMsgSend();
        String ipAddress = bcMsgGetObserver.getIPAddress();
        String login = bcPlayer.getLogin();

        // если процесс приема сообщений остановился
        BCMsgError bcMsgError = (BCMsgError) bcMsgGet.getBCMsg("BCMsgError");

        if(bcMsgError != null) {        

            bcLog.logWrite(ipAddress + ": login: " + login + " error: " + bcMsgError.getError());
            
            bcMsgGet.deleteObserver(bcMsgGetObserver);
            
            bcGame.setStateWonLost();
            
            // удалить игрока Б со стола
            bcTable.setBCPlayerB(null);

            // отправить игроку А объект "Сообщение: Уйти со стола"
            BCPlayer bcPlayerA = bcTable.getBCPlayerA();
            BCMsgSitTableOut bcMsgSitTableOut = new BCMsgSitTableOut(bcTable);
            bcPlayerA.sendBCMsg(bcMsgSitTableOut);

            // удаляю игрока
            bcRoom.removePlayer(login);            
            
            return;
        
        }
        
        // если пришло "Сообщение: Ход"
        BCMsgTurn bcMsgTurn = (BCMsgTurn) bcMsgGet.getBCMsg("BCMsgTurn");

        if(bcMsgTurn != null) {
            
            // если игра уже закончилась
            if (bcGame.getState() != BCGameState.PLAY)
                return;

            // отправляю сообщение сопернику
            bcTable.getBCPlayerA().getBCMsgSend().sendBCMsg(bcMsgTurn);

            // фиксирую ход
            bcGame.turnPlayerB(bcMsgTurn.getBCTurn().getNumber());            
                     
            return;
        
        } 
    
        // если пришло "Сообщение: Сдаться"
        BCMsgLost bcMsgLost = (BCMsgLost) bcMsgGet.getBCMsg("BCMsgLost");
        
        if(bcMsgLost != null) {
                        
            // если игра уже закончилась
            if (bcGame.getState() != BCGameState.PLAY)
                return;

            // устанавливаю статус игры
            bcGame.setStateWonLost();
                    
        } 
    
    }

    @Override
    public void update(Observable o, Object arg) {

        BCGame bcGame = (BCGame) o;
        
        if(bcGame.getState() != BCGameState.PLAY) {
            
            // удаляю наблюдателя игры
            bcGame.deleteObserver(this);
                                    
            // замена объекта состояние объекта наблюдатель за объектом прием сообщений (в игровой комнате)
            BCMsgGetObserverStateInRoom bcMsgGetObserverStateInRoom = new BCMsgGetObserverStateInRoom();
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateInRoom);
        
        }
    
    }
    
}
