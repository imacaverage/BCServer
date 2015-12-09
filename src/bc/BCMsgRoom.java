/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Игровая комната"
 * @author iMacAverage
 */
public class BCMsgRoom extends BCMsg {

    /**
     * объект "Игровая комната"
     */
    private final BCRoom bcRoom;
    
    /**
     * Создать объект
     * @param bcRoom объект "Игровая комната"
     */
    public BCMsgRoom(BCRoom bcRoom) {
        this.bcRoom = bcRoom;
    }
    
    /**
     * Получить объект "Игровая комната"
     * @return объект "Игровая комната"
     */
    public BCRoom getBCRoom() {
        return this.bcRoom;
    }
    
}
