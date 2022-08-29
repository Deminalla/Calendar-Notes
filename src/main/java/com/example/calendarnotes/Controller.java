package com.example.calendarnotes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.time.Month;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    @FXML
    private ChoiceBox<Integer> yearBox;

    @FXML
    private ChoiceBox<Month> monthBox;

    Month[] monthList = Month.values();

    List<Integer> yearList = IntStream.range(1970, 2023).boxed().collect(Collectors.toList());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthBox.getItems().addAll(monthList);
        yearBox.getItems().addAll(yearList);
    }
}
