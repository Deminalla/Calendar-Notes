package com.example.calendarnotes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class allTests {

    HashMap<String, List<String>> calendarNoteList;
    DBCalendar cal = new DBCalendar();

    @Test
    void selectAll() { // maybe i shouldnt even check this
        //assertEquals(9, cal.selectAll().size());
        // make sure for now theres 9
    }
    @Test
    void insert() {
        int size  = cal.selectAll().size();
        calendarNoteList = cal.selectAll();
        cal.insert("06-09-2022", "hi, don't mind me", "0x99cc99ff");
        assertEquals(size+1, cal.selectAll().size());
        assertFalse(calendarNoteList.containsKey("06-09-2022")); // it won't exist in the hashmap before putting it there

        List<String> list_of_props = new ArrayList<>();
        Collections.addAll(list_of_props,"hi, don't mind me", "0x99cc99ff");
        calendarNoteList.put("06-09-2022", list_of_props);
        assertEquals(calendarNoteList, cal.selectAll()); // the correct information has been inserted to DB
    }

    @Test
    void remove() {
        int size  = cal.selectAll().size();
        cal.remove("06-09-2022");
        assertEquals(size-1, cal.selectAll().size());
        calendarNoteList = cal.selectAll();
        assertFalse(calendarNoteList.containsKey("06-09-2022"));
    }

    @ParameterizedTest // execute this test method multiple times with different parameters
    @CsvSource(value = {"19-09-2022, hi, 0xffcc99ff", // this way we can use multiple parameters
            "15-07-1985, whats up, 0xe49ae4ff"})
    void calendarCreateOrUpdate(String dateString, String text, String colour) {
        List<String> list_of_props = new ArrayList<>();
        Collections.addAll(list_of_props,text,colour);
        assertEquals(text, list_of_props.get(0));
        assertEquals(colour, list_of_props.get(1));

        calendarNoteList = Controller.calendarCreateOrUpdate(dateString, text, colour, list_of_props);
        assertTrue(calendarNoteList.containsKey(dateString));
        assertEquals(list_of_props, calendarNoteList.get(dateString));

        int size  = cal.selectAll().size();
        cal.remove(dateString);
        calendarNoteList = cal.selectAll();
        assertFalse(calendarNoteList.containsKey(dateString));
        assertEquals(size-1, cal.selectAll().size());
    }

    @Test
    void firstWeekD(){
        Calendar cal = new GregorianCalendar();
        cal.set(2022, 8, 1);
        assertEquals(4, Controller.firstWeekD(cal));
        cal.set(2021, 1, 1);
        assertEquals(1, Controller.firstWeekD(cal));
    }
}