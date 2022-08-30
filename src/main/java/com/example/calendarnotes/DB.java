package com.example.calendarnotes;

import java.sql.*;
import java.util.HashMap;

public class DB {
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:\\Users\\deniz\\Desktop\\Calendar-Notes\\Cal.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String title, String text) {
        String sql = "INSERT INTO Notes(Title,TextField) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, text);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public HashMap<String, String> selectAll(){
        String sql = "SELECT Title, TextField FROM Notes";
        HashMap<String, String> noteList = new HashMap<>();

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){


            // loop through the result set
            while (rs.next()) {
                String title = rs.getString("Title");
                String text = rs.getString("TextField");
                noteList.put(title, text);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return noteList;
    }

    public static void main(String[] args) {
        // coult be used for testing

        /*DB app = new DB();
        // insert three new rows
        app.insert("Yes", "lmao actually no i lied");
        app.insert("Semifinished Goods", "what is this");
        app.insert("Finished Goods", "nooooo");

        app.select("Semifinished Goods");
        */
    }

}