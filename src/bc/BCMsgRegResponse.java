/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Регистрация: ответ"
 * @author iMacAverage
 */
public class BCMsgRegResponse extends BCMsg {
    
    /**
     * признак успешной регистрации
     */    
    private final boolean registration;
    
    /** 
     * логин 
     */
    private final String login;

    /**
     * рейтинг игрока
     */
    private final int rating;
    
    /**
     * версия клиента
     */
    private final int version;
    
    /**
     * ошибка регистрации
     */
    private final String error;
    
    /**
     * Создать объект
     * @param login логин игрока
     * @param rating рейтинг игрока
     * @param version версия клиента
     */
    public BCMsgRegResponse(String login, int rating, int version) {
        this.registration = true;
        this.login = login;
        this.rating = rating;
        this.version = version;
        this.error = null;
    }
    
    /**
     * Создать объект
     * @param login логин игрока
     * @param error ошибка регистрации
     */
    public BCMsgRegResponse(String login, String error) {
        this.registration = false;
        this.login = login;
        this.rating = -1;
        this.version = -1;
        this.error = error;
    }

    /**
     * Получить признак успешной регистрации
     * @return признак успешной регистрации
     */
    public boolean getRegistration() {
        return this.registration;
    }
    
    /**
     * Получить логин игрока
     * @return логин игрока
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Получить рейтинг игрока
     * @return рейтинг игрока
     */
    public int getRating() {
        return this.rating;
    }
    
    /**
     * Получить версию клиента
     * @return версия клиента
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * Получить ошибку регистрации
     * @return ошибка регистрации
     */
    public String getError() {
        return this.error;
    }
        
}
