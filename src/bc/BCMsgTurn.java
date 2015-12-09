/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Ход"
 * @author iMacAverage
 */
public class BCMsgTurn extends BCMsg {
    
    /**
     * объект "Ход"
     */
    private final BCTurn bcTurn;
    
    /**
     * Создать объект
     * @param bcTurn объект "Ход" 
     */    
    public BCMsgTurn(BCTurn bcTurn) {
        this.bcTurn = bcTurn;
    }
    
    /**
     * Получить объект "Ход"
     * @return объект "Ход"
     */
    public BCTurn getBCTurn() {
        return this.bcTurn;
    }
    
}
