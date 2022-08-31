package com.example.calendarnotes;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DBCalendar {
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

    public void insert(String title, String text, String Date_color) {
        String sql = "INSERT INTO CalendarNotes (Title,TextField,Date_color) VALUES(?,?,?)"; // ? are values that we will initialize later
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, text);
            pstmt.setString(3, Date_color);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String title, String text, String Date_color){
        String sql = "UPDATE CalendarNotes SET Textfield = ?, Date_color = ?, WHERE Title = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text);
            pstmt.setString(2, Date_color);
            pstmt.setString(3, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(String title){
        String sql = "DELETE FROM Notes WHERE Title = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public HashMap<String, List<String>> selectAll(){
        String sql = "SELECT Title, TextField, Date_color FROM CalendarNotes";
        HashMap<String, List<String>> noteList = new HashMap<>();

        List<String> list_of_props = new ArrayList<String>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) { // loop through the result set
                String title = rs.getString("Title");
                String text = rs.getString("TextField");
                String color = rs.getString("Date_color");
                Collections.addAll(list_of_props,text,color);
                noteList.put(title,list_of_props);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return noteList; // return hashmap so you can put all the information to noteList in Controller
    }
}