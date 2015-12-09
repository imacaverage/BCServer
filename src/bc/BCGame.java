/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Класс "Игра"
 * @author iMacAverage
 */
public class BCGame extends Observable {

    /** 
     * загаданное число игрока А 
     */
    private final String numberA;

    /** 
     * загаданное число игрока Б 
     */
    private final String numberB;

    /** 
     * объект "Стол"
     */
    private final BCTable bcTable;

    /** 
     * коллекция объектов "Ход" игрока А
     */
    private final ArrayList<BCTurn> turnsA;

    /** 
     * коллекция объектов "Ход" игрока Б 
     */
    private final ArrayList<BCTurn> turnsB;    

    /** 
     * объект "Состояние игры" 
     */
    private BCGameState state;
    
    /**
     * признак хода игрока А
     */
    private boolean isTurnA;

    /** 
     * Создать объект
     * @param bcTable объект "Стол"
     * @param numberA заданное число игрока А
     * @param numberB заданное число игрока Б
     */
    public BCGame(BCTable bcTable, String numberA, String numberB) {
        super();
        this.bcTable = bcTable;
        this.numberA = numberA;
        this.numberB = numberB;
        this.turnsA = new ArrayList<>();
        this.turnsB = new ArrayList<>();
        this.state = BCGameState.PLAY;
        this.isTurnA = true;
    }

    /**
     * Получить число игрока А
     * @return число игрока А
     */
    public String getNumberA() {
       return numberA;
    }

    /**
     * Получить число игрока Б
     * @return число игрока Б
     */
    public String getNumberB() {
       return numberB;
    }

    /**
     * Получить объект "Стол"
     * @return объект "Стол"
     */
    public BCTable getBCTable() {
        return this.bcTable;
    }
    
    /**
     * Получить признак хода игрока А
     * @return признак хода игрока А
     */
    public synchronized boolean isTurnA() {
        return this.isTurnA;
    }
    
    /** 
     * Выполнить ход игрока А
     * @param number число хода
     * @return объект Хода
     */
    public synchronized BCTurn turnPlayerA(String number) {    
        BCTurn bcTurn = new BCTurn(number, this.numberB);        
        this.turnsA.add(bcTurn);        
        this.isTurnA = false;        
        // вычислить состояние игры
        calcState();            
        this.setChanged();
        this.notifyObservers();
        return bcTurn;
    }

    /** 
     * Выполнить ход игрока Б
     * @param number число хода
     * @return объект Хода
     */
    public synchronized BCTurn turnPlayerB(String number) {
        BCTurn bcTurn = new BCTurn(number, numberA);
        turnsB.add(bcTurn);
        this.isTurnA = true;
        // вычислить состояние игры
        calcState();    
        this.setChanged();
        this.notifyObservers();
        return bcTurn;    
    }

    /** 
     * Вычислить состояние игры
     */    
    protected void calcState(){
        BCTurn lastTurnA, lastTurnB;        
        if(state != BCGameState.PLAY)   // если игра уже закончилась
            return;        
        if(turnsA.size() != turnsB.size()) {    // если игрок Б еще не походил
            state = BCGameState.PLAY;
            return;
        }        
        lastTurnA = (BCTurn) turnsA.get(turnsA.size() - 1); // последний ход игрока А
        lastTurnB = (BCTurn) turnsB.get(turnsB.size() - 1); // последний ход игрока Б
        if(lastTurnA.checkWin() && lastTurnB.checkWin()) {  // если ходы обоих игроков выигрышные
            state = BCGameState.DRAW;
            return;
        }        
        if(lastTurnA.checkWin() && !lastTurnB.checkWin()) {  // если только ход игрока А выигрышный
            state = BCGameState.WON;
            return;
        }        
        if(!lastTurnA.checkWin() && lastTurnB.checkWin()) { // если только ход игрока Б выигрышный
            state = BCGameState.LOST;
            return;
        }        
        state = BCGameState.PLAY;   // иначе игра продолжается
    } 

    /** 
     * Получить объект "Состояние игры"
     * @return объект "Состояние игры"
     */
    public synchronized BCGameState getState() {
        return state;
    }
    
    /** 
     * Задать объект "Состояние игры"
     * @param state объект "Состояние игры"
     */
    public synchronized void setState(BCGameState state) {
        this.state = state;
    }

    /** 
     * Найти подходящее число для следующего хода для режима игры с повторением цифр
     * @param bcTurns предыдущие ходы
     * @return число хода
     */
    protected String getNextTurnWithRepeat(ArrayList<BCTurn> bcTurns) {

        final int n = 10;
        final int m = numberA.length();
        boolean found;
        char[] seq = {'0','1','2','3','4','5','6','7','8','9'};
        char[] number = new char [m];
        int count = (int) Math.pow(n, m);

        // массив номеров перестановок
        int[] numberSeq = new int [count];
        for (int i = 0; i < count; i++)
            numberSeq[i] = i; 
        Comb.sortRandom(numberSeq);

        // случайная сортировка массива
        Comb.sortRandom(seq);
        
        for (int i = 0; i < count; i++) {
            // Получить заданную (по номеру) выборку m из n с перестановкой (с возможностью повторения цифр)
            Comb.getSeqWithRepeat(seq, number, numberSeq[i]);
            found = true;
            for (BCTurn bcTurn : bcTurns) {
                if (bcTurn.getC() != BCTurn.calcC(String.valueOf(number), bcTurn.getNumber()) || bcTurn.getB() != BCTurn.calcB(String.valueOf(number), bcTurn.getNumber())) {
                    found = false;
                    break;
                }    
            }
            if(found)
                return String.valueOf(number);
        }

        // если по каким то причинам подходящее число не найдено
        return null;
    }

    /** 
     * Найти подходящее число для следующего хода для режима игры с без повторения цифр
     * @param bcTurns предыдущие ходы
     * @return число хода
     */
    protected String getNextTurnWithNotRepeat(ArrayList<BCTurn> bcTurns){

        final int n = 10;
        final int m = numberA.length();
        boolean found;
        char[] seq = {'0','1','2','3','4','5','6','7','8','9'};
        char[] number = new char [m];
        int count = Comb.fact(m) * (Comb.fact(n) / (Comb.fact(m) * Comb.fact(n - m)));

        // массив номеров перестановок
        int[] numberSeq = new int [count];
        for (int i = 0; i < count; i++)
            numberSeq[i] = i; 
        Comb.sortRandom(numberSeq);

        // случайная сортировка массива
        Comb.sortRandom(seq);

        for (int i = 0; i < count; i++) {
            // Получить заданную (по номеру) выборку m из n с перестановкой (без повторения цифр)
            Comb.getSeqAMFromN(seq, number, numberSeq[i]);
            found = true;
            for(BCTurn bcTurn : bcTurns) {
                if(bcTurn.getC() != BCTurn.calcC(String.valueOf(number), bcTurn.getNumber()) || bcTurn.getB() != BCTurn.calcB(String.valueOf(number), bcTurn.getNumber())) {
                    found = false;
                    break;
                }    
            }
            if(found)
                return String.valueOf(number);
        }
        return null;
    
    }

    /** 
     * Найти подходящее число для следующего хода
     * @param bcTurns предыдущие ходы
     * @return число хода
     */
    public String getNextTurn(ArrayList<BCTurn> bcTurns) {
        if(bcTable.getWithRepeat())
            return getNextTurnWithRepeat(bcTurns);
        return getNextTurnWithNotRepeat(bcTurns);
    }
    /** 
     * Получить ходы игрока А
     * @return ходы игрока А
     */
    public ArrayList getTurnsA() {
        return turnsA;
    }

    /** 
     * Получить ходы игрока B
     * @return ходы игрока B
     */
    public ArrayList<BCTurn> getTurnsB() {
        return turnsB;
    }

    /**
     * Установить окончание игры - выигрыш по времени
     */
    public synchronized void setStateWonTime() {
        state = BCGameState.WON_TIME;
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Установить окончание игры - проигрыш по времени
     */
    public synchronized void setStateLostTime() {
        state = BCGameState.LOST_TIME;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Установить окончание игры - выигрыш: соперник сдался
     */
    public synchronized void setStateWonLost() {
        state = BCGameState.WON_LOST;
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Установить окончание игры - проигрыш: я сдался
     */
    public synchronized void setStateLostLost() {
        state = BCGameState.LOST_LOST;
        this.setChanged();
        this.notifyObservers();
    }

}
