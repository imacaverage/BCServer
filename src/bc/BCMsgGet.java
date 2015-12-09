/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

/**
 * Класс "Прием сообщений"
 * @author iMacAverage
 */
public class BCMsgGet extends Observable implements Runnable {

    /** 
     * объект "Поток для получения сообщений"
     */
    private final ObjectInputStream inStream;

    /**
     * коллекция объктов "Сообщение"
     */
    private final ArrayList<BCMsg> bcMsgs;

    /**
     * флаг выхода
     */
    private boolean flagExit;
    
    /**
     * Создать объект
     * @param inStream объект "Поток для получения сообщений"
     */
    public BCMsgGet(ObjectInputStream inStream) {
        this.inStream = inStream;
        this.bcMsgs = new ArrayList<>();
        this.flagExit = false;
    }
    
    /**
     * Получить признак выхода
     * @return признак выхода
     */
    public synchronized boolean getExit() {
        return this.flagExit;
    }

    /**
     * Задать признак выхода
     */
    public synchronized void setExit() {
        this.flagExit = true;
    }
    
    /**
     * Получить объект "Сообщение" (возвращаемый объект удаляется из коллекции объектов "Сообщение": кроме "Сообщение: ошибка")
     * @param bcMsgClassName наименование класса сообщения
     * @return объект "Сообщение"
     */
    public synchronized BCMsg getBCMsg(String bcMsgClassName) {
        BCMsg bcMsg;
        for(Iterator<BCMsg> it = this.bcMsgs.iterator(); it.hasNext();) {
            bcMsg = it.next();
            if(bcMsg.getClass().getSimpleName().equals(bcMsgClassName)) {
                if(!bcMsgClassName.equals("BCMsgError"))
                    it.remove();
                return bcMsg;
            }
        }
        return null;
    }

    /**
     * Задать объект "Сообщение"
     * @param bcMsg объект "Сообщение"
     */
    private synchronized void setBCMsg(BCMsg bcMsg) {
        BCMsg bcMsgOld;
        for(Iterator<BCMsg> it = this.bcMsgs.iterator(); it.hasNext();) {
            bcMsgOld = it.next();
            if(bcMsgOld.getClass().getSimpleName().equals(bcMsg.getClass().getSimpleName()))
                it.remove();
        }
        this.bcMsgs.add(bcMsg);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Удалить объект "Сообщение"
     * @param bcMsgClassName наименование класса сообщения
     */
    public synchronized void removeBCMsg(String bcMsgClassName) {
        BCMsg bcMsg;
        for(Iterator<BCMsg> it = this.bcMsgs.iterator(); it.hasNext();) {
            bcMsg = it.next();
            if(bcMsg.getClass().getSimpleName().equals(bcMsgClassName))
                it.remove();
        }
    }

    @Override
    public void run() {
        
        BCMsg bcMsg = null;

        // обработка сообщений
        while(!this.flagExit){
    
            try {
                bcMsg = (BCMsg) inStream.readObject();
            } 
            catch (IOException ex) {
                BCMsgError bcMsgError = new BCMsgError("Connection lost!\n");
                this.setBCMsg(bcMsgError);
                return;
            } 
            catch (ClassNotFoundException ex) {
                BCMsgError bcMsgError = new BCMsgError("Conversion error in the message object!\n");
                this.setBCMsg(bcMsgError);
                return;
            }
            
            this.setBCMsg(bcMsg);
            
        }            

        BCMsgError bcMsgError = new BCMsgError("Connection lost!\n");
        this.setBCMsg(bcMsgError);
    
    }
    
}
