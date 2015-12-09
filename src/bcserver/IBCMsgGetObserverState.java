/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import bc.BCMsgGet;

/**
 * Интерфейс "Состояние объекта 'Наблюдатель объекта Прием сообщений'"
 * @author iMacAverage
 */
public interface IBCMsgGetObserverState {
    
    /**
     * Обработать сообщение
     * @param bcMsgGetObserver объект "Наблюдатель объекта Прием сообщений'"
     * @param bcMsgGet объект "Прием сообщений"
     */
    public void processBCMsg(BCMsgGetObserver bcMsgGetObserver, BCMsgGet bcMsgGet);
    
}
