/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

/**
 * Класс "Регулярные выражения"
 * @author iMacAverage
 */
public class RegEx {

    /**
     * Получить строку регулярного выражения "n цифр"
     * @param n количество цифр
     * @return строка регулярного выражения
     */
    public static String digitsWithRepetition(int n) {
        return "^\\d{" + n + "}$";
    }

    /**
     * Получить строку регулярного выражения "n цифр без повторений"
     * @param n количество цифр
     * @return строка регулярного выражения
     */
    public static String digitsWithOutRepetition(int n) {
        return "^(?!.*(.).*\\1)\\d{" + n +"}$";
    }
    
}
