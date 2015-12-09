/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Игра: конец"
 * @author iMacAverage
 */
public class BCMsgGameEnd extends BCMsg {
    
    /**
     * объект "Состояние игры"
     */
    private final BCGameState bcGameState;
    
    /**
     * Создать объект
     * @param bcGameState объект "Состояние игры"
     */
    public BCMsgGameEnd(BCGameState bcGameState) {
        this.bcGameState = bcGameState;
    }
    
    /**
     * Получить объект "Состояние игры"
     * @return объект "Состояние игры"
     */
    public BCGameState getBCGameState() {
        return this.bcGameState;
    }
    
}
