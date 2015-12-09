/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Класс "Стол"
 * @author iMacAverage
 */
public class BCTable implements Serializable {

    /** 
     * номер стола 
     */
    private int numTable;

    /** 
     * объект "Игрок" А 
     */
    private final BCPlayer bcPlayerA;
    
    /** 
     * объект "Игрок" Б 
     */
    private BCPlayer bcPlayerB;
    
    /** 
     * минимальный рейтинг для соперника 
     */
    private final int minRating;

    /** 
     * длина числа 
     */
    private final int numberLength;
    
    /** 
     * игра с возможностью повторения цифр в числе 
     */
    private final boolean withRepeat;
    
    /** 
     * количество минут на игру 
     */
    private final int countMin;
    
    /**
     * коллекция запрещенных для данного стола объектов "Игрок"
     */
    private final ArrayList<BCPlayer> bcPlayersBanned;
    
    /** 
     * Создать объект стола игры
     * @param numTable номер стола
     * @param bcPlayer объект "Игрок" А
     * @param minRating минимальный рейтинг для соперника
     * @param numberLength длина числа
     * @param withRepeat игра с возможностью повторения цифр в числе
     * @param countMin количество минут на игру
     */
    public BCTable(int numTable, BCPlayer bcPlayer, int minRating, int numberLength, boolean withRepeat, int countMin) {
        super();
        this.numTable = numTable;
        this.bcPlayerA = bcPlayer;
        this.bcPlayerB = null;
        this.minRating = minRating;
        this.numberLength = numberLength;
        this.withRepeat = withRepeat;
        this.countMin = countMin;
        this.bcPlayersBanned = new ArrayList<>();
    }
    
    /** 
     * Создать объект (без номера)
     * @param bcPlayer объект "Игрок" А
     * @param minRating минимальный рейтинг для соперника
     * @param numberLength длина числа
     * @param withRepeat игра с возможностью повторения цифр в числе
     * @param countMin количество минут на игру
     */
    public BCTable(BCPlayer bcPlayer, int minRating, int numberLength, boolean withRepeat, int countMin) {
        super();
        this.numTable = 0;
        this.bcPlayerA = bcPlayer;
        this.bcPlayerB = null;
        this.minRating = minRating;
        this.numberLength = numberLength;
        this.withRepeat = withRepeat;
        this.countMin = countMin;
        this.bcPlayersBanned = new ArrayList<>();
    }

    /**
     * Получить номер стола
     * @return номер стола
     */
    public synchronized int getNumTable() {
        return numTable;
    }    

    /**
     * Задать номер стола
     * @param numTable номер стола
     */
    public synchronized void setNumTable(int numTable) {
        this.numTable = numTable;
    }    

    /**
     * Получить минимальный рейтинг для соперника
     * @return минимальный рейтинг для соперника
     */
    public int getMinRating() {
        return minRating;
    }
    
    /**
     * Получить длину числа игры
     * @return длина числа
     */
    public int getNumberLength() {
        return numberLength;
    }    

    /**
     * Получить признак возможности повторения цифр в числе
     * @return признак возможности повторения цифр в числе
     */
    public boolean getWithRepeat() {
        return withRepeat;
    }

    /**
     * Получить количество минут на игру
     * @return количество минут на игру
     */
    public int getCountMin() {
        return countMin;
    }
    
    /**
     * Получить объект "Игрок" А
     * @return объект "Игрок" А
     */
    public BCPlayer getBCPlayerA() {
        return bcPlayerA;
    }

    /**
     * Получить объект "Игрок" Б
     * @return объект "Игрок" Б
     */
    public synchronized BCPlayer getBCPlayerB() {
        return this.bcPlayerB;
    }
    
    /**
     * Проверить забанен ли игрок на столе
     * @param bcPlayer объект "Игрок"
     * @return true в случае успеха, иначе false
     */
    public synchronized boolean checkBanned(BCPlayer bcPlayer) {
        for(BCPlayer bcPlayerBanned : this.bcPlayersBanned)
            if(bcPlayerBanned.getLogin().equals(bcPlayer.getLogin()))
                return true;
        return false;
    }

    /**
     * Задать объект "Игрок" Б
     * @param bcPlayer "Игрок" Б
     */
    public synchronized void setBCPlayerB(BCPlayer bcPlayer) {
        this.bcPlayerB = bcPlayer;
    }
    
    /**
     * Выгнать игрока Б со стола
     */
    public synchronized void kickBCPlayerB() {
        this.bcPlayersBanned.add(this.bcPlayerB);
        this.bcPlayerB = null;
    }
    
}
