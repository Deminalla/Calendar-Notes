package com.example.calendarnotes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.Month;

import java.net.URL;
import java.util.*;
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

    @FXML
    private ColorPicker color;
    private TextArea textCN;

    @FXML
    private Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24,b25,b26,b27,b28,b29,b30,b31,b32,b33,b34,b35,b36,b37;
    private List<Button> list = new ArrayList<>();
    Button currentButton = new Button(); // keep track of which day button was clicked last

    Month[] monthList = Month.values();
    List<Integer> yearList = IntStream.range(1970, 2023).boxed().collect(Collectors.toList());
    HashMap<String, String> noteList = new HashMap<>();
    HashMap<String, String> calendarNoteList = new HashMap<>();
    DB notesInfo = new DB();
    DB calendarNotesInfo = new DB();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthBox.getItems().addAll(monthList);
        yearBox.getItems().addAll(yearList);
        titleBox.setOnAction(this::retrieveNote);

        noteList.putAll(notesInfo.selectAll("Notes")); // add data from database
        Set<String> keys = noteList.keySet();
        titleBox.getItems().addAll(keys); // this will show the titles in the choicebox

        calendarNoteList.putAll(calendarNotesInfo.selectAll("CalendarNotes"));


        Collections.addAll(list, b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24,b25,b26,b27,b28,b29,b30,b31,b32,b33,b34,b35,b36,b37);
        LocalDate currentdate = LocalDate.now();
        int currentMonth = currentdate.getMonthValue();
        int currentYear = currentdate.getYear();

        monthBox.setValue(Month.of(currentMonth));
        yearBox.setValue(currentYear);
        printCalendarMonthYear(currentMonth, currentYear, list);

        int currentDay = currentdate.getDayOfMonth();
        for (Button button:list) {
            if (button.getText().equals(currentDay+"")){
                button.fire();
            }
        }
        monthBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                printCalendarMonthYear((Integer) number2+1, yearBox.getValue(), list);
            }
        });
        yearBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                printCalendarMonthYear(monthBox.getValue().getValue(),(Integer) number2+1970, list);
            }
        });
    }

    private static void printCalendarMonthYear(int month, int year,List<Button> butonlist) {
        for (Button button:butonlist) {
            button.setText("");
        }
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(year, month-1, 1);
        int firstWeekdayOfMonth = cal.get(Calendar.DAY_OF_WEEK)-1;
        int numberOfMonthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        firstWeekdayOfMonth = ((firstWeekdayOfMonth == 0) ? 7 : firstWeekdayOfMonth);
        printCalendar(numberOfMonthDays, firstWeekdayOfMonth, butonlist);
    }
    private static void printCalendar(int numberOfMonthDays, int firstWeekdayOfMonth,List<Button> butonlist) {
        int day_of_month = 1;

        for (int j =0; j<37;j++){
            if(j>=firstWeekdayOfMonth-1&&j<numberOfMonthDays+firstWeekdayOfMonth-1){
                butonlist.get(j).setVisible(true);
                butonlist.get(j).setText(day_of_month+"");
                day_of_month++;
            }
            else{
               butonlist.get(j).setVisible(false);
            }
        }
    }

    @FXML
    void Button(ActionEvent event){
        for (Button button:list) {
            button.getStyleClass().removeAll("active");
            button.getStyleClass().add("button");
        }
        Object node = event.getSource();
        Button b = (Button)node;

        b.getStyleClass().add("active");
        color.setOnAction(new EventHandler() {
            @FXML
            public void handle(Event t) {
                b.setStyle("-fx-background-color: "+color.getValue().toString().replace("0x", "#")+";");
            }
        });
        b.setStyle("-fx-border-color: #ff396e;");
        currentButton = b;
        retrieveCalendarNote();
        }

    private void retrieveCalendarNote() {
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);

        textCN.setText(calendarNoteList.get(dateString));

    }

    @FXML
    void createCalendarNode (ActionEvent event) {
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);

        String text = textCN.getText();
        if(!calendarNoteList.containsKey(dateString)){
            calendarNoteList.put(dateString, text);
            calendarNotesInfo.insert(dateString, text, "CalendarNotes");
        }
        else{
            calendarNoteList.put(dateString, text);
            calendarNotesInfo.update(dateString, text, "CalendarNotes");
        }
    }

    @FXML
    void deleteCalendarNote(ActionEvent event) {
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);

        calendarNoteList.remove(dateString);
        calendarNotesInfo.remove(dateString);
        textCN.clear();
    }

    @FXML
    void createNote(ActionEvent event) {
        String title = titleN.getText();
        String text = textN.getText();
        if(!noteList.containsKey(title)){
            noteList.put(title, text);
            titleBox.getItems().addAll(title);
            notesInfo.insert(title, text, "Notes");
        }
        else{
            noteList.put(title, text);
            notesInfo.update(title, text, "Notes");
        }
    }

    public void retrieveNote(ActionEvent event){
        String title = titleBox.getValue();
        titleN.setText(title);
        textN.setText(noteList.get(title));
    }

    @FXML
    void deleteNote(ActionEvent event){
        String title = titleN.getText();
        noteList.remove(title);
        titleBox.getItems().remove(title);
        notesInfo.remove(title);

        textN.clear();
        titleN.clear();
    }

    @FXML
    void clear(ActionEvent event) {
        titleN.clear();
        textN.clear();
    }
}
