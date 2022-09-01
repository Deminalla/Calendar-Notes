package com.example.calendarnotes;

import java.sql.*;
import java.util.HashMap;

public class DB {
    private Connection connect() {
        String url = "jdbc:sqlite:src\\main\\resources\\Cal.db"; // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String title, String text, String tableName) {
        String sql = "INSERT INTO " + tableName + " (Title,TextField) VALUES(?,?)"; // ? are values that we will initialize later

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, text);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String title, String text, String tableName){
        String sql = "UPDATE " + tableName + " SET Textfield = ? WHERE Title = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text);
            pstmt.setString(2, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(String title, String tableName){
        String sql = "DELETE FROM " + tableName + " WHERE Title = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public HashMap<String, String> selectAll(String tableName){
        String sql = "SELECT Title, TextField FROM " + tableName;
        HashMap<String, String> noteList = new HashMap<>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) { // loop through the result set
                String title = rs.getString("Title");
                String text = rs.getString("TextField");
                noteList.put(title, text);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return noteList; // return hashmap so you can put all the information to noteList in Controller
    }

    public static void main(String[] args) {
        // could be used for testing

        /*DB app = new DB();
        // insert three new rows
        app.insert("Yes", "lmao actually no i lied");
        app.insert("Semifinished Goods", "what is this");
        app.insert("Finished Goods", "nooooo");

        app.select("Semifinished Goods");
        */
    }
}