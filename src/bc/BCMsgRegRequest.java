/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Регистрация: запрос"
 * @author iMacAverage
 */
public class BCMsgRegRequest extends BCMsg {
    
    /**
     * логин
     */
    private final String login;
    
    /**
     * пароль
     */
    private final String password;

    /** 
     * признак создания нового игрока 
     */
    private final boolean newPlayer;
    
    /**
     * Создать объект
     * @param login логин
     * @param password пароль
     * @param newPlayer признак создания нового игрока
     */
    public BCMsgRegRequest(String login, String password, boolean newPlayer) {
        this.login = login;
        this.password = password;
        this.newPlayer = newPlayer;
    }
    
    /**
     * Получить логин
     * @return логин
     */
    public String getLogin() {
        return this.login;
    }
    
    /**
     * Получить пароль
     * @return пароль
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Получить признак создания нового игрока
     * @return признак создания нового игрока
     */
    public boolean getNewPlayer() {
        return this.newPlayer;
    }
    
}
