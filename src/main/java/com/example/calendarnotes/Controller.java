package com.example.calendarnotes;

import com.itextpdf.text.*;
import javafx.beans.value.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.FileNotFoundException;
import java.time.*;

import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    @FXML
    private Tab tabCN;

    @FXML
    private Tab tabN;

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
    private TextArea textCN;

    @FXML
    private Label warningN, warningCN; // place to show warnings to user

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
        warningN.setStyle("-fx-text-fill: red;");
        warningCN.setStyle("-fx-text-fill: red;");

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
                textCN.clear(); // clear so when changing month/year, the textArea doesn't stay the same until you pick a date
            }
        });
        yearBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                printCalendarMonthYear(monthBox.getValue().getValue(),(Integer) number2+1970, list);
                textCN.clear();
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
        clearWarnings();
        for (Button button:list) {
            button.setStyle("-fx-border-color: black;");
        }
        Object node = event.getSource();
        Button b = (Button)node;
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
        clearWarnings();
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
        clearWarnings();
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);

        calendarNoteList.remove(dateString);
        calendarNotesInfo.remove(dateString, "CalendarNotes");
        textCN.clear();
    }

    @FXML
    void createNote(ActionEvent event) {
        clearWarnings();
        String title = titleN.getText();
        if(title != null && !(title.isEmpty())) {
            String text = textN.getText();
            if (!noteList.containsKey(title)) {
                noteList.put(title, text);
                titleBox.getItems().addAll(title);
                notesInfo.insert(title, text, "Notes");
            } else {
                noteList.put(title, text);
                notesInfo.update(title, text, "Notes");
            }
        } else {
            warningN.setText("Title is empty");
        }
    }

    public void retrieveNote(ActionEvent event){
        String title = titleBox.getValue();
        titleN.setText(title);
        textN.setText(noteList.get(title));
    }

    @FXML
    void deleteNote(ActionEvent event){
        clearWarnings();
        String title = titleN.getText();
        noteList.remove(title);
        titleBox.getItems().remove(title);
        notesInfo.remove(title, "Notes");

        textN.clear();
        titleN.clear();
    }

    @FXML
    void clear(ActionEvent event) {
        clearWarnings();
        titleN.clear();
        textN.clear();
    }

    @FXML
    void exportToPDF(ActionEvent event) throws DocumentException, FileNotFoundException {
        clearWarnings();
        if (tabN.isSelected()) { // to know from which tab we are exporting (because then the keys are different)
            if ((!titleN.getText().isEmpty()) && (!textN.getText().isEmpty())) {
                String title = titleN.getText();
                String text = textN.getText();
                PDF pdfN = new PDF();
                pdfN.exportToPDF(title, text);
            } else {
                warningN.setText("Title or text is empty");
            }
        } else if (tabCN.isSelected()){
            if (textCN.getText() != null &&  !(textCN.getText().isEmpty())) {
                DateInfo date= new DateInfo();
                String dateString = date.dateFormat(currentButton, monthBox, yearBox);
                String text = textCN.getText();
                PDF pdfN = new PDF();
                pdfN.exportToPDF(dateString, text);
            } else {
                warningCN.setText("Text is empty");
            }
        }
    }
    public void clearWarnings(){
        warningN.setText("");
        warningCN.setText("");
    }
}