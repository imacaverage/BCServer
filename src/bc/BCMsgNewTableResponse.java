/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Создать стол: ответ"
 * @author iMacAverage
 */
public class BCMsgNewTableResponse extends BCMsg {
    
    /**
     * номер стола
     */
    private final int numTable;
    
    /**
     * Создать объект
     * @param numTable номер стола
     */    
    public BCMsgNewTableResponse(int numTable) {
        this.numTable = numTable;
    }
    
    /**
     * Получить номер стола
     * @return номер стола
     */
    public int getNumTable() {
        return this.numTable;
    }
    
}
