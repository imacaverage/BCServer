/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

/**
 * Класс "Игровая комната"
 * @author iMacAverage
 */
public class BCRoom extends Observable implements Serializable {
    
    /** 
     * коллекция объектов "Стол"
     */
    private final ArrayList<BCTable> bcTables;    
    
    /** 
     * коллекция объектов "Игрок"
     */
    private final ArrayList<BCPlayer> bcPlayers;
    
    /** 
     * свободный номер стола 
     */
    private int numTable;
    
    /** 
     * Создать объект
     */
    public BCRoom() {
        super();
        this.numTable = 1;
        this.bcTables = new ArrayList<>();
        this.bcPlayers = new ArrayList<>();
    }

    /**
     * Получить коллекцию объектов "Игрок"
     * @return коллекцию объектов "Игрок"
     */
    public ArrayList<BCPlayer> getBCPlayers() {
        return bcPlayers;
    }

    /**
     * Получить коллекцию объектов "Стол"
     * @return коллекцию объектов "Стол"
     */
    public ArrayList<BCTable> getBCTables() {
        return bcTables;
    }
    
    /** Поиск игрока по логину
     * @param login логин игрока
     * @return объект игрока если игрок найден, иначе null 
     */
    public BCPlayer findPlayer(String login) {
        for(BCPlayer bcPlayer : this.bcPlayers)
            if(login.equals(bcPlayer.getLogin()))
                return bcPlayer;
        return null;
    }
    
    /** Поиск стола по номеру
     * @param numTable номер стола
     * @return объект стола если стол найден, иначе null 
     */
    public BCTable findTable(int numTable) {
        for(BCTable bcTable : this.bcTables)
            if(bcTable.getNumTable() == numTable)
                return bcTable;
        return null;
    }

    /** Поиск стола по логину игрока
     * @param login логин игрока 
     * @param playerA true если это игрок А, иначе false
     * @return объект стола если стол найден, иначе null 
     */
    public BCTable findTable(String login, boolean playerA) {
        for(BCTable bcTable : this.bcTables)
            if(login.equals(playerA? bcTable.getBCPlayerA().getLogin() : bcTable.getBCPlayerB().getLogin()))
                return bcTable;
        return null;
    }

    /** Добавить объект "Стол"
     * @param bcTable объект "Стол"
     */
    public synchronized void addTable(BCTable bcTable) {
        bcTables.add(bcTable);
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Удалить объект "Стол"
     * @param numTable номер стола
     */
    public synchronized void removeTable(int numTable) {
        for (Iterator<BCTable> it = this.bcTables.iterator(); it.hasNext();) {
            BCTable bcTable = it.next();
            if(bcTable.getNumTable() == numTable) {
                it.remove();
                this.setNumTable(); // установить свободный номер стола
                this.setChanged();
                this.notifyObservers();
                return;
            }
        }
    }

    /** Добавить объект "Игрок"
     * @param bcPlayer объект "Игрок"
     */
    public synchronized void addPlayer(BCPlayer bcPlayer) {
        bcPlayers.add(bcPlayer);
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Удалить объект "Игрок"
     * @param login логин игрока
     */
    public synchronized void removePlayer(String login) {
        for (Iterator<BCPlayer> it = this.bcPlayers.iterator(); it.hasNext();) {
            BCPlayer bcPlayer = it.next();
            if (login.equals(bcPlayer.getLogin())) {
                it.remove();
                this.setChanged();
                this.notifyObservers();
                return;
            }
        }
    }
    
    /**
     * Задать состояние изменения
     */
    public synchronized void setChange() {
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Получить свободный номер стола
     * @return свободный номер стола
     */
    public synchronized int getNumTable() {
        numTable++;
        return (numTable - 1);
    }

    /**
     * Установить свободный номер стола
     */
    public synchronized void setNumTable() {
        int maxNumTable = 0;
        for (BCTable bcTable : bcTables) {
            if(maxNumTable < bcTable.getNumTable())
                maxNumTable = bcTable.getNumTable();
        }
        numTable = maxNumTable + 1;
    }

}
