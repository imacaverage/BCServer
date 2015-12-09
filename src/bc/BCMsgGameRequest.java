/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Игра: запрос"
 * @author iMacAverage
 */
public class BCMsgGameRequest extends BCMsg {

    /**
     * объект "Стол"
     */
    private final BCTable bcTable;
    
    /** 
     * загаданное число игрока игрока А
     */
    private final String numberA;
    
    /** 
     * загаданное число игрока игрока Б
     */
    private final String numberB;

    /**
     * Создать объект
     * @param bcTable объект "Стол"
     * @param numberA загаданное число игрока игрока А
     * @param numberB загаданное число игрока игрока Б
     */
    public BCMsgGameRequest(BCTable bcTable, String numberA, String numberB) {
        this.bcTable = bcTable;
        this.numberA = numberA;
        this.numberB = numberB;
    }
    
    /**
     * Создать объект
     * @param bcTable объект "Стол"
     * @param numberB загаданное число игрока игрока Б
     */
    public BCMsgGameRequest(BCTable bcTable, String numberB) {
        this.bcTable = bcTable;
        this.numberA = null;
        this.numberB = numberB;
    }
    
    /**
     * Получить объект "Стол"
     * @return объект "Стол"
     */
    public BCTable getBCTable() {
        return this.bcTable;
    }
    
    /**
     * Получить загаданное число игрока А
     * @return загаданное число игрока А
     */
    public String getNumberA() {
        return this.numberA;
    }
    
    /**
     * Получить загаданное число игрока Б
     * @return загаданное число игрока Б
     */
    public String getNumberB() {
        return this.numberB;
    }

}
