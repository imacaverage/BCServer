/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.util.Observable;
import java.util.Observer;

/**
 * Класс "Наблюдатель объекта 'Прием сообщений': 'Пинг: запрос'"
 * Наблюдатель объекта "Прием сообщений": "Пинг: запрос"
 * @author iMacAverage
 */
public class BCMsgGetObserverPingRequest implements Observer {
    
    /**
     * объект "Отправка сообщений"
     */
    private final BCMsgSend bcMsgSend;
    
    /**
     * Создать объект
     * @param bcMsgSend "Отправка сообщений"
     */
    public BCMsgGetObserverPingRequest(BCMsgSend bcMsgSend) {
        this.bcMsgSend = bcMsgSend;
    }        

    @Override
    public void update(Observable o, Object arg) {
        BCMsgGet bcMsgGet = (BCMsgGet) o;
        BCMsgPingRequest bcMsgPingRequest = (BCMsgPingRequest) bcMsgGet.getBCMsg("BCMsgPingRequest");
        if(bcMsgPingRequest == null)
            return;
        BCMsgPingResponse bcMsgPingResponse = new BCMsgPingResponse();
        this.bcMsgSend.sendBCMsg(bcMsgPingResponse);
    }
    
}
