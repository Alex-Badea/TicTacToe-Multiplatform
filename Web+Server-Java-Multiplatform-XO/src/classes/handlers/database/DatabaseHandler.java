/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.handlers.database;

import java.sql.*;
import classes.handlers.database.misc.*;

/**
 * Clasa ce contine metode intermediare care faciliteaza conexiunea dintre Server si Baza de date, obtinandu-se
 * fluxul de informatii necesare
 */
public class DatabaseHandler {
    private static DatabaseHandler ourInstance = new DatabaseHandler();

    public static DatabaseHandler getInstance() {
        return ourInstance;
    }

    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASS;

    ////Initializeaza baza de date
    private DatabaseHandler() {
        DB_URL = "jdbc:mysql://127.0.0.1/database";
        DB_USER = "admin";
        DB_PASS = "admin";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Ia ca parametru username-ul si parola userului curent, cripteaza parola si compara cu rezultatele obtinute
    // din baza de date.
    public boolean checkUsernameAndPasswordMatch(String username, String password) {
        String encryptedPassword = Encryptor.encrypt(password, "ILOVEROCKANDROLL");

        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = connection.prepareStatement("SELECT encryptedPassword FROM users WHERE username = ?")) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getString("encryptedPassword").equals(encryptedPassword);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ia ca parametru username-ul si parola introduse de player la Signup, cripteaza parola si salveaza in baza de date
    // noul cont de utilizator
    public boolean addNewUsernameAndPassword(String username, String password){
        if("".equals(username.trim()) || "".equals(password.trim()))
            return false;
        if(username.matches(".*[{}!@#$%^&*()`~_=+;:'|\".>,</?\\]\\[\\-].*"))
            return false;
        String encryptedPassword = Encryptor.encrypt(password, "ILOVEROCKANDROLL");

        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username, encryptedPassword) VALUES (?, ?)")) {

            ps.setString(1, username);
            ps.setString(2, encryptedPassword);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Persistarea match-ului in baza de date, nume jucator X, nume jucator O si castigatorul
    public void insertMatchInDatabase(String xPlayer, String oPlayer, String winningPlayer, Timestamp startDate, Timestamp endDate, String tableStateJson){
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO matches (xPlayer, oPlayer, winningPlayer, startDate, endDate, tableState) VALUES (?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, xPlayer);
            ps.setString(2, oPlayer);
            ps.setString(3, winningPlayer);
            ps.setTimestamp(4, startDate);
            ps.setTimestamp(5, endDate);
            ps.setString(6, tableStateJson);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementGamesPlayed(String username) {
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET gamesPlayed = gamesPlayed + 1 WHERE username = ?")) {
            ps.setString(1, username);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementGamesWon(String username) {
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET gamesWon = gamesWon + 1 WHERE username = ?")) {
            ps.setString(1, username);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementGamesLost(String username) {
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET gamesLost = gamesLost + 1 WHERE username = ?")) {
            ps.setString(1, username);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
