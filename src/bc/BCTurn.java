/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.io.Serializable;

/**
 * Класс "Ход"
 * @author iMacAverage
 */
public class BCTurn implements Serializable {

    /** 
     * число хода 
     */
    private final String numberTurn;

    /** 
     * результат хода, количество коров (цифр не на своем месте) 
     */
    private final int countC;

    /** 
     * результат хода, количество быков (цифр на своем месте) 
     */
    private final int countB;    

    /**
     * Создать объект
     * @param numberTurn число хода
     * @param countC количество коров (цифр не на своих местах)
     * @param countB количество быков (цифр на своих местах)
     */
    public BCTurn(String numberTurn, int countC, int countB) {
       super();
       this.numberTurn = numberTurn;
       this.countC = countC;
       this.countB = countB;
    }

    /**
     * Создать объект
     * @param numberTurn число хода
     * @param numberOriginal загаданное число
     */
    public BCTurn(String numberTurn, String numberOriginal) {
       super();
       this.numberTurn = numberTurn;
       this.countC = calcC(numberTurn, numberOriginal);
       this.countB = calcB(numberTurn, numberOriginal);
    }

    /**
     * Получить число хода
     * @return число хода
     */
    public String getNumber() {
       return this.numberTurn;
    }

    /**
     * Получить количество коров
     * @return количество коров (количество угаданных цифр, которые расположены не на своих местах)
     */
    public int getC() {
       return this.countC;
    }
    
    /**
     * Получить количество быков
     * @return количество коров (количество угаданных цифр, которые расположены на своих местах)
     */
    public int getB() {
       return this.countB;
    }
    
    /**
     * Проверить является ли ход выигрышным
     * @return true - если ход выигрышный, иначе false
     */
    public boolean checkWin(){
       return this.countB == this.numberTurn.length();
    }
   
    /**
     * Вычислить количество коров
     * @param numberTurn число хода
     * @param numberOriginal загаданное число
     * @return количество коров (количество угаданных цифр, которые расположены не на своих местах)
     */    
    static public int calcC(String numberTurn, String numberOriginal) {
        StringBuilder numberTurnWithOutB = new StringBuilder(numberTurn);
        StringBuilder numberOriginalWithOutB = new StringBuilder(numberOriginal);
        // удаляю всех быков
	for (int i = 0; i < numberTurnWithOutB.length();) {
            if (numberTurnWithOutB.charAt(i) == numberOriginalWithOutB.charAt(i)) {
                numberTurnWithOutB.deleteCharAt(i);
                numberOriginalWithOutB.deleteCharAt(i);
            }
            else {
                i++;
            }
        }        
	return calcCWithOutB(numberTurnWithOutB, numberOriginalWithOutB);
    }

    static private int calcCWithOutB(StringBuilder numberTurn, StringBuilder numberOriginal) {
	int i, index, countC;
	for (i = 0, countC = 0; i < numberTurn.length(); i++) {
            index = numberOriginal.indexOf(numberTurn.substring(i, i + 1));
            if(index != -1) {
                countC += 1 + calcCWithOutB(numberTurn.deleteCharAt(i), numberOriginal.deleteCharAt(index));
            }
        }
	return countC;
    }
    
    /**
     * Вычислить количество быков
     * @param numberTurn число хода
     * @param numberOriginal загаданное число
     * @return количество быков (количество угаданных цифр, которые расположены на своих местах)
     */
    static public int calcB(String numberTurn, String numberOriginal) {
	int countB = 0;
	for(int i = 0; i < numberTurn.length(); i++)
            if(numberOriginal.charAt(i) == numberTurn.charAt(i))
                countB++;
	return countB;
    }

}