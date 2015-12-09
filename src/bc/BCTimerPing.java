/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

/**
 * Класс "Пинг на таймере"
 * Наблюдатель объекта "Прием сообщений"
 * @author iMacAverage
 */
public class BCTimerPing extends TimerTask implements Observer {

    /**
     * объект "Прием сообщений"
     */
    private final BCMsgGet bcMsgGet;

    /**
     * объект "Отправка сообщений"
     */
    private final BCMsgSend bcMsgSend;
    
    /** 
     * количество ответов пинга 
     */
    private long pingResponse;

    /** 
     * количество запросов пинга 
     */
    private long pingRequest;
        
    /**
     * Создать объект
     * @param bcMsgGet объект "Прием сообщений"
     * @param bcMsgSend объект "Отправка сообщений" 
     */
    public BCTimerPing(BCMsgGet bcMsgGet, BCMsgSend bcMsgSend) {
        this.bcMsgGet = bcMsgGet;
        this.bcMsgSend = bcMsgSend;
        this.pingRequest = 0;
        this.pingResponse = 0;
    }
    
    /**
     * Выполнить пинг
     * @return признак успешности пинга
     */
    public boolean ping() {
        // число запросов и ответов пинг-сообщений не совпадают
        if(this.pingRequest != this.pingResponse)
            return false;
        // запрос пинга
        BCMsgPingRequest bcMsgPingRequest = new BCMsgPingRequest();
        if(!this.bcMsgSend.sendBCMsg(bcMsgPingRequest))
            return false;
        // увеличиваю счетчик запросов пинг-сообщений
        this.pingRequest++;
        return true;
    }
   
    @Override
    public void run() {
        if(!this.ping()) {
            this.bcMsgGet.setExit();
            this.cancel();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        BCMsgGet bcMsgGet = (BCMsgGet) o;
        BCMsgError bcMsgError = (BCMsgError) bcMsgGet.getBCMsg("BCMsgError");
        if(bcMsgError != null) {
            this.cancel();
            return;
        }
        BCMsgPingResponse bcMsgPingResponse = (BCMsgPingResponse) bcMsgGet.getBCMsg("BCMsgPingResponse");
        if(bcMsgPingResponse != null)
            this.pingResponse++;
    }
    
}
