/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCGame;
import bc.BCGameState;
import bc.BCMsgTime;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

/**
 * Класс таймера игры
 * наблюдатель объекта "Игра"
 * @author iMacAverage
 */
public class BCTimerGame extends TimerTask implements Observer {

    /**
     * объект "Игра"
     */
    private final BCGame bcGame;
    
    /**
     * время игрока (секунд)
     */
    private final int time;
    
    /**
     * потрачено времени игроком А (милисекунд)
     */
    private long lostTimeA;

    /**
     * потрачено времени игроком Б (милисекунд)
     */
    private long lostTimeB;
    
    /**
     * начало измерения
     */
    private long startTime;

    /**
     * Создать объект
     * @param bcGame объект "Игра"
     */
    public BCTimerGame(BCGame bcGame) {
        this.bcGame = bcGame;
        this.time = bcGame.getBCTable().getCountMin() * 60;
        this.lostTimeA = 0;
        this.lostTimeB = 0;
        this.startTime = System.currentTimeMillis(); 
    }
    
    @Override
    public void run() {

        long curTime = System.currentTimeMillis(); 

        if(bcGame.isTurnA()) 
            this.lostTimeA += (curTime - this.startTime);
        else
            this.lostTimeB += (curTime - this.startTime);
        
        this.startTime = curTime;

        int timeA = this.time - (int) (this.lostTimeA / 1000);
        int timeB = this.time - (int) (this.lostTimeB / 1000);
    
        if(timeA <= 0) {
            timeA = 0;
            bcGame.setStateLostTime();
            this.cancel();
        }
        
        if(timeB <= 0) {
            timeB = 0;
            bcGame.setStateWonTime();
            this.cancel();
        }

        BCMsgTime bcMsgTime = new BCMsgTime(timeA, timeB);
        this.bcGame.getBCTable().getBCPlayerA().getBCMsgSend().sendBCMsg(bcMsgTime);
        this.bcGame.getBCTable().getBCPlayerB().getBCMsgSend().sendBCMsg(bcMsgTime);
            
    }

    @Override
    public void update(Observable o, Object arg) {        
        BCGame bcGame = (BCGame) o;
        if(bcGame.getState() != BCGameState.PLAY) {
            this.cancel();    
            bcGame.deleteObserver(this);
        }
    }
    
}
