/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Сесть за стол: ответ"
 * @author iMacAverage
 */
public class BCMsgSitTableResponse extends BCMsg {

    /**
     * объект "Игрок"
     */
    private final BCPlayer bcPlayer;
    
    /**
     * объект "Стол"
     */
    private final BCTable bcTable;
    
    /**
     * ошибка попытки сесть за стол
     */
    private final String error;
    
    /**
     * Создать объект
     * @param bcPlayer объект "Игрок"
     * @param bcTable объект "Стол"
     */
    public BCMsgSitTableResponse(BCPlayer bcPlayer, BCTable bcTable) {
        this.bcPlayer = bcPlayer;
        this.bcTable = bcTable;
        this.error = null;
    }
    
    /**
     * Создать объект
     * @param bcPlayer объект "Игрок"
     * @param bcTable объект "Стол"
     * @param error ошибка попытки сесть за стол
     */
    public BCMsgSitTableResponse(BCPlayer bcPlayer, BCTable bcTable, String error) {
        this.bcPlayer = bcPlayer;
        this.bcTable = bcTable;
        this.error = error;
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
    
    /**
     * Получить ошибку попытки сесть за стол
     * @return ошибка попытки сесть за стол
     */
    public String getError() {
        return this.error;
    }
    
}
