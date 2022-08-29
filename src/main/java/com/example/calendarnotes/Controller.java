package com.example.calendarnotes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.Month;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    @FXML
    private ChoiceBox<Integer> yearBox;

    @FXML
    private ChoiceBox<Month> monthBox;

    @FXML
    private ChoiceBox<String> titleBox;

    @FXML
    private TextArea textN;

    @FXML
    private TextField titleN;

    Month[] monthList = Month.values();
    List<Integer> yearList = IntStream.range(1970, 2023).boxed().collect(Collectors.toList());
    HashMap<String ,String> noteList = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthBox.getItems().addAll(monthList);
        yearBox.getItems().addAll(yearList);
        titleBox.setOnAction(this::retrieveNote);
    }

    @FXML
    void createNote(ActionEvent event) {
        String title = titleN.getText();
        String text = textN.getText();
        noteList.put(title, text);
        titleBox.getItems().addAll(title);
    }

    public void retrieveNote(ActionEvent event){
        String title = titleBox.getValue();
        titleN.setText(title);
        textN.setText(noteList.get(title));
    }

    @FXML
    void clear(ActionEvent event) {
        titleN.clear();
        textN.clear();
    }
}
