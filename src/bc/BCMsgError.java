/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: ошибка"
 * @author iMacAverage
 */
public class BCMsgError extends BCMsg {
    
    /**
     * Текст ошибки
     */    
    private final String error;
    
    /**
     * Создать объект
     * @param error текст ошибки
     */
    public BCMsgError(String error) {
        this.error = error;
    }
    
    /**
     * Получить текст ошибки
     * @return текст ошибки
     */    
    public String getError() {
        return this.error;
    }
    
}
