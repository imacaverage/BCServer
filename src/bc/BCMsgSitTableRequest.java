/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Сесть за стол: запрос"
 * @author iMacAverage
 */
public class BCMsgSitTableRequest extends BCMsg {
    
    /**
     * объект "Игрок"
     */
    private final BCPlayer bcPlayer;
    
    /**
     * объект "Стол"
     */
    private final BCTable bcTable;
    
    /**
     * Создать объект
     * @param bcPlayer объект "Игрок"
     * @param bcTable объект "Стол"
     */
    public BCMsgSitTableRequest(BCPlayer bcPlayer, BCTable bcTable) {
        this.bcPlayer = bcPlayer;
        this.bcTable = bcTable;
    }
    
    /**
     * Получить объект "Игрок"
     * @return объект "Игрок"
     */
    public BCPlayer getBCPlayer() {
        return this.bcPlayer;
    }
    
    /**
     * Получить объект "Стол"
     * @return объект "Стол"
     */
    public BCTable getBCTable() {
        return this.bcTable;
    }
    
}
