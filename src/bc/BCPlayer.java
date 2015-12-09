/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.io.Serializable;

/**
 * Класс "Игрок"
 * @author iMacAverage
 */
public class BCPlayer implements Serializable {
    
    /** 
     * логин 
     */
    private final String login;

    /** 
     * рейтинг 
     */
    private int rating;
    
    /**
     * объект "Отправка сообщений"
     */
    private transient final BCMsgSend bcMsgSend;

    /** Создать объект
     * @param login логин игрока
     * @param rating рейтинг игрока
     * @param bcMsgSend объект "Отправка сообщений"
     */
    public BCPlayer(String login, int rating, BCMsgSend bcMsgSend) {
        this.login = login;
        this.rating = rating;
        this.bcMsgSend = bcMsgSend;
    }

    /** 
     * Получить логин игрока
     * @return логин игрока
     */
    public String getLogin() {
        return login;
    }

    /**
     * Получить рейтинг игрока
     * @return рейтинг игрока
     */
    public synchronized int getRating() {
        return rating;
    }
    
    /**
     * Задать рейтинг игрока
     * @param rating рейтинг игрока
     */
    public synchronized void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Получить объект "Отправка сообщений"
     * @return объект "Отправка сообщений"
     */
    public BCMsgSend getBCMsgSend() {
        return this.bcMsgSend;
    }
            
    /**
     * Отправить сообщение
     * @param bcMsg объект "Сообщение"
     * @return true в случае успеха, иначе false
     */
    public boolean sendBCMsg(BCMsg bcMsg) {
        return this.bcMsgSend.sendBCMsg(bcMsg);
    }
    
}
