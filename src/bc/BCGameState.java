/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Состояние игры"
 * @author iMacAverage
 */
public enum BCGameState {

    /** 
     * игра еще не закончена 
     */
    PLAY,

    /** 
     * ничья 
     */
    DRAW,

    /** 
     * победа 
     */
    WON,

    /** 
     * победа по времени 
     */
    WON_TIME,

    /** 
     * победа: соперник сдался 
     */
    WON_LOST,

    /** 
     * проигрыш 
     */
    LOST,

    /** 
     * проигрыш по времени 
     */
    LOST_TIME,

    /** 
     * проигрыш: я сдался
     */
    LOST_LOST

}
