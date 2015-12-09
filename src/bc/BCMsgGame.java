/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Сообщение: Игра"
 * @author iMacAverage
 */
public class BCMsgGame extends BCMsg {   
    
    /**
     * объект "Игра"
     */
    private final BCGame bcGame;
    
    /**
     * Создать объект
     * @param bcGame объект "Игра"
     */
    public BCMsgGame(BCGame bcGame) {
        this.bcGame = bcGame;
    }
    
    /**
     * Получить объект "Игра"
     * @return объект "Игра"
     */
    public BCGame getBCGame() {
        return this.bcGame;
    }
    
}
