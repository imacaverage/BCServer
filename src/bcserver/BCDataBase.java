/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс "База данных"
 * @author iMacAverage
 */
public class BCDataBase {

    /** 
     * объект "Соединение с базой даных"
     */
    protected Connection conn;

    /**
     * Создать объект
     * @param strConnect строка подключения к базе данных
     * @param login логин 
     * @param password пароль
     * @throws SQLException
     */
    public BCDataBase(String strConnect, String login, String password) throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        this.conn = DriverManager.getConnection(strConnect, login, password);
    }

    /**
     * Поиск игрока в базе данных
     * @param login логин игрока
     * @return true если игрок найден, иначе false
     * @throws java.sql.SQLException
     */
    public boolean findPlayer(String login) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pStat;
        pStat = this.conn.prepareStatement("select login from players where login = ?");
        pStat.setString(1, login);
        if(pStat.execute())
            rs = pStat.getResultSet();
        return rs.next();
    }    

    /**
     * Добавить игрока в базу данных
     * @param login логин игрока
     * @param password пароль игрока
     * @throws SQLException
     */
    public void addPlayer(String login, String password) throws SQLException {
        PreparedStatement pStat;
        pStat = this.conn.prepareStatement("insert into players (login, password, rating) values (?, ?, 1000)");
        pStat.setString(1, login);
        pStat.setString(2, password);
        pStat.execute();
    }

    /**
     * Проверка пароля игрока
     * @param login логин игрока
     * @param password пароль игрока
     * @return true пароль верный, иначе false
     * @throws java.sql.SQLException
     */
    public boolean checkPassword(String login, String password) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pStat;
        pStat = this.conn.prepareStatement("select login from players where login = ? and password = ?");
        pStat.setString(1, login);
        pStat.setString(2, password);
        if(pStat.execute())
            rs = pStat.getResultSet();
        return rs.next();
    }    

    /**
     * Получить рейтинг игрока
     * @param login логин игрока
     * @return рейтинг игрока
     * @throws java.sql.SQLException
     */
    public int getRating(String login) throws SQLException {
        int rating = 0;
        ResultSet rs;
        PreparedStatement pStat;
        pStat = this.conn.prepareStatement("select * from players where login = ?");
        pStat.setString(1, login);
        if(pStat.execute()) {
            rs = pStat.getResultSet();
            if(rs.next()) {
                rating = rs.getInt("RATING");
            } 
        }
        return rating;
    }
    
    /**
     * Задать рейтинг игрока
     * @param login логин игрока
     * @param rating рейтинг игрока
     * @throws SQLException 
     */
    public void setRating(String login, int rating) throws SQLException {
        PreparedStatement pStat;
        pStat = this.conn.prepareStatement("update players set rating = ? where login = ?");
        pStat.setString(1, String.valueOf(rating));
        pStat.setString(2, login);
        pStat.execute();
    } 

}