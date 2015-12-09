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
import bc.BCMsgGameEnd;
import bc.BCMsgGet;
import bc.BCMsgLost;
import bc.BCMsgNewTableCancel;
import bc.BCMsgSend;
import bc.BCMsgTurn;
import bc.BCPlayer;
import bc.BCRoom;
import bc.BCTable;
import java.util.Observable;
import java.util.Observer;

/**
 * Класс "Состояние: Игра: игрок А"
 * Обрабатывает сообщения:
 * Сообщение: Ошибка
 * Сообщение: Сдаться
 * Сообщение: Ход
 * Наблюдатель объекта "Игра"
 * @author iMacAverage
 */
public class BCMsgGetObserverStateGameA implements IBCMsgGetObserverState, Observer {

    /**
     * объект "Наблюдатель за объектом 'Прием сообщений'"
     */
    private final BCMsgGetObserver bcMsgGetObserver;
    
    /**
     * Создать объект 
     * @param bcMsgGetObserver объект "Наблюдатель за объектом 'Прием сообщений'"
     */
    public BCMsgGetObserverStateGameA(BCMsgGetObserver bcMsgGetObserver) {
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
            
            bcGame.setStateLostLost();
            
            BCPlayer bcPlayerB = bcTable.getBCPlayerB();
            
            // отправить игроку Б объект "Сообщение: Отмена создания стола"
            if(bcPlayerB != null) {
                BCMsgNewTableCancel bcMsgNewTableCancel = new BCMsgNewTableCancel(bcTable);
                bcPlayerB.sendBCMsg(bcMsgNewTableCancel);
            }
            
            // удалить стол из игровой комнаты
            bcRoom.removeTable(bcTable.getNumTable());

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
            bcTable.getBCPlayerB().getBCMsgSend().sendBCMsg(bcMsgTurn);

            // фиксирую ход
            bcGame.turnPlayerA(bcMsgTurn.getBCTurn().getNumber());            
         
            return;
        
        } 
    
        // если пришло "Сообщение: Сдаться"
        BCMsgLost bcMsgLost = (BCMsgLost) bcMsgGet.getBCMsg("BCMsgLost");
        
        if(bcMsgLost != null) {
            
            // если игра уже закончилась
            if (bcGame.getState() != BCGameState.PLAY)
                return;

            // устанавливаю статус игры
            bcGame.setStateLostLost();
                            
        } 

    }

    @Override
    public void update(Observable o, Object arg) {

        BCTable bcTable = this.bcMsgGetObserver.getBCTable();

        BCGame bcGame = (BCGame) o;
        
        if(bcGame.getState() != BCGameState.PLAY) {
            
            // удаляю наблюдателя игры
            bcGame.deleteObserver(this);
            
            // отправляю сообщения игрокам об окончании игры
            BCMsgGameEnd bcMsgGameEnd = new BCMsgGameEnd(bcGame.getState());
            bcTable.getBCPlayerA().getBCMsgSend().sendBCMsg(bcMsgGameEnd);
            bcTable.getBCPlayerB().getBCMsgSend().sendBCMsg(bcMsgGameEnd);
            
            // обновляю рейтинги игроков
            this.bcMsgGetObserver.getBCServer().updateRating(bcGame, this.bcMsgGetObserver.getBCDataBase());
                        
            // удаляю игру
            this.bcMsgGetObserver.getBCServer().removeBCGame(bcTable.getNumTable());
            
            // удаляю стол
            this.bcMsgGetObserver.getBCRoom().removeTable(bcTable.getNumTable());
            
            // замена объекта состояние объекта наблюдатель за объектом прием сообщений (в игровой комнате)
            BCMsgGetObserverStateInRoom bcMsgGetObserverStateInRoom = new BCMsgGetObserverStateInRoom();
            bcMsgGetObserver.setIBCMsgGetObserverState(bcMsgGetObserverStateInRoom);
        
        }
    
    }
    
}
