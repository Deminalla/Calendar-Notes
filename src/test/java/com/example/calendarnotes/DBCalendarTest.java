package com.example.calendarnotes;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DBCalendarTest {
    DBCalendar cal = new DBCalendar();

    @Test
    void selectAll() {
        assertEquals(2, cal.selectAll().size());
    }

    @Test
    void insert() {
        cal.insert("06-09-2022", "hi, don't mind me", "0x99cc99ff");
        assertEquals(3, cal.selectAll().size());
        // check if the intformation that was inserted is correct
    }

    @Test
    void update() {
    }

    @Test
    void remove() {
        cal.remove("06-09-2022");
        assertEquals(2, cal.selectAll().size());
    }

}