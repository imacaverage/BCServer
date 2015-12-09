/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Время"
 * @author iMacAverage
 */
public class BCMsgTime extends BCMsg {
    
    /**
     * время игрока А
     */
    private final int timeA;
    
    /**
     * время игрока Б
     */
    private final int timeB;

    /**
     * Создать объект
     * @param timeA время игрока А
     * @param timeB время игрока Б
     */    
    public BCMsgTime(int timeA, int timeB) {
        this.timeA = timeA;
        this.timeB = timeB;
    }
    
    /**
     * Получить время игрока А
     * @return время игрока А
     */
    public int getTimeA() {
        return this.timeA;
    }
    
    /** Получить время игрока Б
     * @return время игрока Б
     */
    public int getTimeB() {
        return this.timeB;
    }

}
