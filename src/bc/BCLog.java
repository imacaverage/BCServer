/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Класс "Лог"
 * @author iMacAverage
 */
public class BCLog {

    /** 
     * объект "Файл лога"
     */
    private final FileWriter logFile;
 
    /**
     * Создать объект
     * @param logFile объект "Файл лога"
     */
    public BCLog(FileWriter logFile) {
        this.logFile = logFile;
    }
    
    /**
     * Записать сообщение в лог
     * @param message сообщение
     */
    public synchronized void logWrite(String message) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            this.logFile.write(dateFormat.format(calendar.getTime()) + " " + message);
            this.logFile.flush();
        } 
        catch (IOException ex) {
            System.exit(1);
        }
    }

    /**
     * Закрыть лог
     */
    public void logClose() {
        if(logFile != null) {
            try {
                logFile.close();
            } 
            catch (IOException ex) {
                System.exit(1);
            }
        }    
    }
    
}
