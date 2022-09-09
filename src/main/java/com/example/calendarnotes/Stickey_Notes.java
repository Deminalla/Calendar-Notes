package com.example.calendarnotes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Stickey_Notes {
    @FXML
    private AnchorPane notes_tab;
    @FXML
    private  AnchorPane disp_dab;
    @FXML
    private  Button add;
    @FXML
    private  Button edit;
    @FXML
    private  Button delete;

    @FXML
    private TextField title_field;
    @FXML
    private Label warningN;
    private int row,column = 0;
    int curent_notes_count;
    private WebEngine engine;

    private List<Button> list = new ArrayList<>();
    private String currentTitle = "";

    Button currentButton = new Button();
    DB db = new DB();
    public HashMap<String, String> noteList = new HashMap<>();

    double row_notes_count=5;
    public GridPane notes_init(GridPane grid) {
        row = 0;
        column = 0;
        list.clear();
        grid.getChildren().clear();
        curent_notes_count=0;
        noteList = db.selectAll();
        List<String> titles = new ArrayList<String>(noteList.keySet());
        while (curent_notes_count<db.getProfilesCount()){
            Button button = new Button(titles.get(curent_notes_count));
            button.setMinWidth(100);
            button.setMinHeight(120);
            button.setMaxSize(100,200);
            button.wrapTextProperty().setValue(true);
            button.setOnAction(buttonHandler);
            list.add(button);
            grid.add(button, column, row);
            column++;
            if(column>row_notes_count){
                column = 0;
                row++;
            }
            curent_notes_count++;
        }
        return grid;
    }
    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            notes_tab.setVisible(true);
            disp_dab.setVisible(false);
            edit.setVisible(true);
            add.setVisible(false);
            delete.setVisible(true);

            Object node = event.getSource();
            Button b = (Button)node;
            String title = b.getText();
            currentTitle = b.getText();
            currentButton = b;

            warningN.setText("");
            title_field.setText(title);
            setPage(noteList.get(title));
        }
    };

    public GridPane add(GridPane grid, String text) {
        Button button = new Button(text);
        button.setMinWidth(100);
        button.setMinHeight(120);
        button.setMaxSize(100,200);
        button.wrapTextProperty().setValue(true);
        button.setOnAction(buttonHandler);
        list.add(button);
        grid.add(button, column, row);
        column++;
        if(column>row_notes_count){
            column = 0;
            row++;
        }
        return grid;
    }
    public void resize(GridPane grid,double screen_width) {
        row = 0;
        column = 0;
        grid.getChildren().clear();
        row_notes_count = (screen_width/105)-1;
        for (Button button:list) {
            grid.add(button, column, row);
            column++;
            if(column>row_notes_count){
                column = 0;
                row++;
            }
        }
    }
    public void update(String title) {
        currentButton.setText(title);
        currentButton.setOnAction(buttonHandler);
    }

    public void setNotes_tab(AnchorPane notes_tab) {
        this.notes_tab = notes_tab;
    }

    public void setDisp_dab(AnchorPane disp_dab) {
        this.disp_dab = disp_dab;
    }

    public void setAdd(Button add) {
        this.add = add;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public void setEngine(WebEngine engine) {
        this.engine = engine;
    }

    public void setTitle_field(TextField title_field) {
        this.title_field = title_field;
    }

    public void setWarningN(Label warningN) {
        this.warningN = warningN;
    }
    public String getCurrentTitle() {
        return currentTitle;
    }
    @FXML
    void setPage(String html){
        engine.executeScript("$('#SummernoteText').summernote('reset');"); // engine.executeScript("$('#SummernoteText').summernote('code', '');");
        engine.executeScript("set('<p><br></p>')");
        engine.executeScript("set('"+html+"')");

    }
}