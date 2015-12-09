/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Класс "Отправка сообщений"
 * @author iMacAverage
 */
public class BCMsgSend {
    
    /** 
     * объект "Поток для отправки сообщений" 
     */
    private final ObjectOutputStream outStream;    
    
    /**
     * Создать объект
     * @param outStream объект "Поток для отправки сообщений" 
     */
    public BCMsgSend(ObjectOutputStream outStream) {
        this.outStream = outStream;
    }

    /**
     * Отправить объект "Сообщение"
     * @param bcMsg объект "Сообщение"
     * @return true в случае успеха, иначе false
     */
    public synchronized boolean sendBCMsg(BCMsg bcMsg) {
        try {
            this.outStream.writeObject(bcMsg);
            this.outStream.flush();
            this.outStream.reset();
        } 
        catch (IOException ex) {
            return false;
        } 
        return true;
    }
    
}
