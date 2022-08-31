package com.example.calendarnotes;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.time.Month;

public class DateInfo {

    public String dateFormat(Button currentButton, ChoiceBox<Month> monthBox, ChoiceBox<Integer> yearBox){
        int currentDay = Integer.parseInt(currentButton.getText());
        String currentDay0 = String.format("%02d" , currentDay);
        int currentMonth = monthBox.getValue().getValue();
        String currentMonth0 = String.format("%02d" , currentMonth);
        int currentYear = yearBox.getValue();

        return currentDay0 + "-" + currentMonth0 + "-" + currentYear;
    }

}
