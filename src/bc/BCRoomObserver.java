/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Класс "Наблюдатель объекта 'Игровая комната'"
 * Наблюдатель объекта "Игровая комната"
 * @author iMacAverage
 */
public class BCRoomObserver implements Observer {
    
    @Override
    public void update(Observable o, Object arg) {
        BCRoom bcRoom = (BCRoom) o;
        BCMsgRoom bcMsgRoom = new BCMsgRoom(bcRoom);
        ArrayList<BCPlayer> bcPlayers = bcRoom.getBCPlayers();
        for(BCPlayer bcPlayer : bcPlayers)
            bcPlayer.sendBCMsg(bcMsgRoom);
    }
    
}
