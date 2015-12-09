/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.util.Random;

/**
 * Класс "Комбинаторика"
 * @author iMacAverage
 */
public class Comb {

    /**
     * Получить заданную (по номеру) перестановку (без повторения цифр)<br>
     * Описание алгоритма:<br>
     * все переставноки получаем путем вставки первого элемента во все позиции во всех перестановках подмассива, который начинается со второго элемента
     * @param seq исходная последовательность цифр
     * @param i начальный индекс в исходной последовательности 
     * @param length длина последовательности
     * @param n номер перестановки, которую надо получить
     */
    static private void getSeq(char[] seq, int i, int length, int n) {
        if(length == 1)
            return;
        // получаю искомую перестановку для подпоследовательности (со следующего элемента)
        getSeq(seq, i + 1, length - 1, n / length);
        // вставляю начальный элемент последовательности в необходимую позицию
        char c = seq[i];
        for(int j = 0; j < n % length; j++)
            seq[i + j] = seq[i + j + 1];
        seq[i + n % length] = c;       
    }

    /**
     * Получить заданную (по номеру) перестановку (без повторения цифр)
     * @param seq исходная последовательность цифр
     * @param n номер перестановки, которую надо получить
     */
    static public void getSeq(char[] seq, int n) {
        getSeq(seq, 0, seq.length, n);
    }    

    /**
     * Вычислить факториал
     * @param i число
     * @return факториал числа
     */
    static public int fact(int i) {
        return (i <= 1)? 1 : i * fact(i - 1); 
    }

    /**
     * Получить заданную (по номеру) выборку m из n (без повторения цифр)<br>
     * Описание алгоритма:<br>
     * на каждом шаге составляем выборки по m из первого элемента из n и всех выборок по (m - 1) из оставшихся (n - 1) элементов
     * @param seqN массив цифр, из которого происходит выбор
     * @param indexN начальный индекс в массиве seqN (для данного шага выполенения)
     * @param n количество элементов в seqN
     * @param seqM массив цифр, в который происходит выбор
     * @param indexM начальный индекс в массиве seqM (для данного шага выполенения)
     * @param m количество элементов в seqM
     * @param i номер выборки, которую необходимо получить
     */    
    static private void getSeqCMFromN(char[] seqN, int indexN, int n, char[] seqM, int indexM, int m, int i) {
        if(m == 0) // если не нужно ничего выбирать
            return;
        if(m == n) { // если один вариант выбора
            System.arraycopy(seqN, indexN, seqM, indexM, n);
            return;
        }
        // количество выборок из подмассива (выборки по (m - 1) из (n - 1))
        int count = fact(n - 1) / (fact(m - 1) * fact(n - m));
        if(i < count) { // если искомая выборка на данном шаге, то берем первый элемент и подвыборку из подмассива
            seqM[indexM++] = seqN[indexN];
            m--;
        }
        else // если искомая выборка не на этом шаге, то вычитаю из номера выборки количество выборок на данном шаге
            i -= count;
        getSeqCMFromN(seqN, indexN + 1, n - 1, seqM, indexM, m, i);            
    }

    /**
     * Получить заданную (по номеру) выборку m из n (без повторения цифр) - C(m из n)
     * @param seqN массив цифр, из которого происходит выбор
     * @param n количество элементов в seqN
     * @param seqM массив цифр, в который происходит выбор
     * @param m количество элементов в seqM
     * @param i номер выборки, которую необходимо получить
     */
    static public void getSeqCMFromN(char[] seqN, int n, char[] seqM, int m, int i) {
        getSeqCMFromN(seqN, 0, n, seqM, 0, m, i);
    }

    /**
     * Получить заданную (по номеру) выборку m из n с перестановкой (без повторения цифр) - A(m из n)
     * @param seqN массив цифр, из которого происходит выбор
     * @param seqM массив цифр, в который происходит выбор
     * @param i номер выборки, которую необходимо получить
     */
    static public void getSeqAMFromN(char[] seqN, char[] seqM, int i) {
        getSeqCMFromN(seqN, 0, seqN.length, seqM, 0, seqM.length, i / fact(seqM.length));
        getSeq(seqM, i % fact(seqM.length));
    }

    /**
     * Получить заданную (по номеру) выборку m из n с перестановкой (с возможностью повторения цифр)
     * @param seqN массив цифр, из которого происходит выбор
     * @param seqM массив цифр, в который происходит выбор
     * @param i номер выборки, которую необходимо получить
     */
    static public void getSeqWithRepeat(char[] seqN, char[] seqM, int i) {
        for(int j = seqM.length - 1; j >= 0; j--, i /= seqN.length)
            seqM[j] = seqN[i % seqN.length];
    }
    
    /**
     * Случайная сортировка массива
     * @param seq массив цифр
     */
    static public void sortRandom(char[] seq) {
        int j;
        char c;
        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < seq.length; i++) {
            j = rnd.nextInt(seq.length - i) + i;
            c = seq[i];
            seq[i] = seq[j];
            seq[j] = c;        
        }
    }

    /**
     * Случайная сортировка массива
     * @param seq массив целых чисел
     */
    static public void sortRandom(int[] seq) {
        int j;
        int c;
        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < seq.length; i++) {
            j = rnd.nextInt(seq.length - i) + i;
            c = seq[i];
            seq[i] = seq[j];
            seq[j] = c;        
        }
    }

    /**
     * Случайное число (массив целых чисел)
     * @param seq массив цифр
     * @param withRepeat возможность повторения цифр
     * @return массив цифр
     */
    static public char[] seqRandom(char[] seq, boolean withRepeat) {
        int j;
        int n = 10;
        Random rnd = new Random(System.currentTimeMillis());
        char[] number = {'0','1','2','3','4','5','6','7','8','9'};
        for (int i = 0; i < seq.length; i++) {
            if (withRepeat == false)
                n--;
            j = rnd.nextInt(n);
            seq[i] = number[j];
            if(withRepeat == false) {
                number[j] = number[n - 1];
                number[n - 1] = seq[i];
            }
        }
        return seq;
    }
    
}
