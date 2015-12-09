/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Отмена создания стола"
 * @author iMacAverage
 */
public class BCMsgNewTableCancel extends BCMsg {

    /**
     * объект "Стол"
     */
    private final BCTable bcTable;
    
    /**
     * Создать объект
     * @param bcTable объект "Стол" 
     */    
    public BCMsgNewTableCancel(BCTable bcTable) {
        this.bcTable = bcTable;
    }
    
    /**
     * Получить объект "Стол"
     * @return объект "Стол"
     */
    public BCTable getBCTable() {
        return this.bcTable;
    }
    
}
