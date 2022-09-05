package com.example.calendarnotes;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stickey_Notes {
    private int row,column = 0;
    int curent_notes_count;
    DB db = new DB();
    HashMap<String, String> noteList = new HashMap<>();
    public GridPane notes_init(GridPane grid) {
        curent_notes_count=0;
        noteList = db.selectAll("Notes");
        List<String> titles = new ArrayList<String>(noteList.keySet());
        while (curent_notes_count<db.getProfilesCount()){
            Button button = new Button(titles.get(curent_notes_count));
            button.setMinWidth(100);
            button.setMinHeight(120);
            grid.add(button, column, row);
            column++;
            if(column>5){
                column = 0;
                row++;
            }
            curent_notes_count++;
        }
        return grid;
    }
    public GridPane add(GridPane grid) {
        Button button = new Button(row + ":" + column);
        button.setMinWidth(100);
        button.setMinHeight(120);
        grid.add(button, column, row);
        column++;
        if(column>5){
            column = 0;
            row++;
        }
        return grid;
    }
}
