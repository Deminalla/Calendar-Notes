package com.example.calendarnotes;

import com.itextpdf.text.*;
import javafx.beans.value.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.time.*;

import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.input.TouchPoint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Controller implements Initializable {

    //web stuff
    @FXML
    private WebView webView;

    private WebEngine engine;
    //

    @FXML
    private Tab tabCN, tabN;

    @FXML
    private ChoiceBox<Integer> yearBox;

    @FXML
    private ChoiceBox<Month> monthBox;

    @FXML
    private ChoiceBox<String> titleBox;

    @FXML
    private TextField titleN;

    @FXML
    private TextArea textCN;
    
    @FXML
    private ColorPicker color;
    
    @FXML
    private  Label date_text;

    @FXML
    private Label warningN, warningCN; // place to show warnings to user

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField searchBar;

    @FXML
    private TextArea searchResult;

    @FXML
    private Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24,b25,b26,b27,b28,b29,b30,b31,b32,b33,b34,b35,b36,b37;
    private List<Button> list = new ArrayList<>();
    Button currentButton = new Button(); // keep track of which day button was clicked last
    String currentTitle = null;

    Month[] monthList = Month.values();
    List<Integer> yearList = IntStream.range(1970, 2023).boxed().collect(Collectors.toList());
    HashMap<String, String> noteList = new HashMap<>();
    HashMap<String, List<String>> calendarNoteList = new HashMap<>();
    DB notesInfo = new DB();
    DBCalendar DBCalendar = new DBCalendar();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //web stuff
        engine = webView.getEngine();
        loadPage();
        //

        monthBox.getItems().addAll(monthList);
        yearBox.getItems().addAll(yearList);
        titleBox.setOnAction(this::retrieveNote);

        noteList.putAll(notesInfo.selectAll()); // add data from database
        Set<String> keys = noteList.keySet();
        titleBox.getItems().addAll(keys); // this will show the titles in the choicebox

        calendarNoteList.putAll(DBCalendar.selectAll());

        textCN.setWrapText(true); // will get rid of horizontal scrollbar and automatically continue text to the next line
        searchResult.setWrapText(true);


        Collections.addAll(list, b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24,b25,b26,b27,b28,b29,b30,b31,b32,b33,b34,b35,b36,b37);
        LocalDate currentdate = LocalDate.now();
        int currentMonth = currentdate.getMonthValue();
        int currentYear = currentdate.getYear();
        set_weekend_days_red(list);

        monthBox.setValue(Month.of(currentMonth));
        yearBox.setValue(currentYear);
        printCalendarMonthYear(currentMonth, currentYear, list);
//        updateButtons(list);

        int currentDay = currentdate.getDayOfMonth();
        for (Button button:list) {
            if (button.getText().equals(currentDay+"")){
                button.fire();
            }
        }
        monthBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                monthBox.setValue(Month.of((Integer)number2+1));
                printCalendarMonthYear((Integer) number2+1, yearBox.getValue(), list);
                textCN.clear(); // clear so when changing month/year, the textArea doesn't stay the same until you pick a date
            }
        });
        yearBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                yearBox.setValue((Integer) number2+1970);
                printCalendarMonthYear(monthBox.getValue().getValue(),(Integer) number2+1970, list);
                textCN.clear();
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String selectedDate = listView.getSelectionModel().getSelectedItem();
                if (selectedDate!=null) { // this method gets called when trying to unselect item so it can be null in that case

                    String textResult = calendarNoteList.get(selectedDate).get(0);
                    searchResult.setText(textResult);
                }
            }
        });
    }


    private void printCalendarMonthYear(int month, int year,List<Button> butonlist) {
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
    private void printCalendar(int numberOfMonthDays, int firstWeekdayOfMonth,List<Button> butonlist) {
        int day_of_month = 1;
        DateInfo dateContr = new DateInfo();
        for (int j =0; j<37;j++){
            butonlist.get(j).setStyle("-fx-background-color: #ffffff");
            if(j>=firstWeekdayOfMonth-1&&j<numberOfMonthDays+firstWeekdayOfMonth-1){
                butonlist.get(j).setVisible(true);
                butonlist.get(j).setText(day_of_month+"");
                String dateString = dateContr.dateFormat(butonlist.get(j), monthBox, yearBox);
                if (!(calendarNoteList.get(dateString) == null)){
                    butonlist.get(j).setStyle("-fx-background-color: "+calendarNoteList.get(dateString).get(1).replace("0x", "#")+";");
                }
                day_of_month++;
            }
            else{
               butonlist.get(j).setVisible(false);
            }
        }

    }
    private void set_weekend_days_red(List<Button> list) {
       list.get(5).getStyleClass().add("weekend_red");
       list.get(6).getStyleClass().add("weekend_red");
       list.get(12).getStyleClass().add("weekend_red");
       list.get(13).getStyleClass().add("weekend_red");
       list.get(19).getStyleClass().add("weekend_red");
       list.get(20).getStyleClass().add("weekend_red");
       list.get(26).getStyleClass().add("weekend_red");
       list.get(27).getStyleClass().add("weekend_red");
    }
    
    @FXML
    void Button(ActionEvent event){
        clearWarnings();
        for (Button button:list) {
            button.getStyleClass().removeAll("active");
            button.getStyleClass().add("buttonn");
        }
        Object node = event.getSource();
        Button b = (Button)node;

        b.getStyleClass().add("active");
        currentButton = b;
        retrieveCalendarNote();
        }

    private void retrieveCalendarNote() {
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);
        try {
            color.setValue(Color.valueOf(calendarNoteList.get(dateString).get(1)));
        } catch (Exception e) {
            color.setValue(Color.valueOf("#ffffff"));
        }
        try {
            textCN.setText(calendarNoteList.get(dateString).get(0));
        } catch (Exception e) {
            textCN.setText("");
        }
        date_text.setText(dateString);
    }
    @FXML
    void createCalendarNode (ActionEvent event) {
        clearWarnings();
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);

        String text = textCN.getText();
        String colour = color.getValue().toString();
        List<String> list_of_props = new ArrayList<>();
        Collections.addAll(list_of_props,text,colour);
        currentButton.setStyle("-fx-background-color: "+color.getValue().toString().replace("0x", "#")+";");
        if(!calendarNoteList.containsKey(dateString)){
            calendarNoteList.put(dateString, list_of_props);
            DBCalendar.insert(dateString,text,colour );
        }
        else{
            calendarNoteList.put(dateString, list_of_props);
            DBCalendar.update(dateString,text,colour);
        }
    }
    @FXML
    void deleteCalendarNote(ActionEvent event) {
        clearWarnings();
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);
        currentButton.setStyle("-fx-background-color: #ffffff");
        color.setValue(Color.valueOf("#ffffff"));
        calendarNoteList.remove(dateString);
        DBCalendar.remove(dateString);
        textCN.clear();
    }

    @FXML
    void createNote(ActionEvent event) {
        clearWarnings();
        String title = titleN.getText();
        currentTitle = title;
        if(title != null && !(title.isEmpty())) {
            String text = getPage();
            if (!noteList.containsKey(title)) {
                noteList.put(title, text);
                titleBox.getItems().addAll(title);
                notesInfo.insert(title, text);
                titleBox.setValue(title);
            } else {
                noteList.put(title, text);
                notesInfo.update(title, text);
            }
        } else {
            warningN.setText("Title is empty");
        }
    }

    public void retrieveNote(ActionEvent event){
            String title = titleBox.getValue();
            currentTitle = title;
            titleN.setText(title);
            setPage(noteList.get(title));
    }

    @FXML
    void deleteNote(ActionEvent event){
        clearWarnings();
        String title = titleN.getText();
        noteList.remove(title);
        titleBox.getItems().remove(title);
        titleBox.setValue(null);
        notesInfo.remove(title);

        clearPage();
        titleN.clear();
    }

    @FXML
    void changeTitle(ActionEvent event) {
        clearWarnings();
        String title = titleN.getText();

        if(title != null && !(title.isEmpty())) {
            String text = getPage();
            if (!noteList.containsKey(title)) {
                noteList.put(title, text);
                titleBox.getItems().addAll(title);
                titleBox.getItems().remove(currentTitle);
                notesInfo.updateTitle(currentTitle, title);
                titleBox.setValue(title);
            } else {
                warningN.setText("Title already exists");
            }
        } else {
            warningN.setText("Title is empty");
        }
        currentTitle = title;
    }

    @FXML
    void exportToPDF(ActionEvent event) throws DocumentException, IOException {
        clearWarnings();
        if (tabN.isSelected()) { // to know from which tab we are exporting (because then the keys are different)
            if (!titleN.getText().isEmpty()) {
                //String title = titleN.getText();
                String text = getPage();
                PDF pdfN = new PDF();
                pdfN.exportHTMLToPDF(text);
            } else {
                warningN.setText("Title or text is empty");
            }
        } else if (tabCN.isSelected()){
            if (textCN.getText() != null &&  !(textCN.getText().isEmpty())) {
                DateInfo date= new DateInfo();
                String dateString = date.dateFormat(currentButton, monthBox, yearBox);
                String text = textCN.getText();
                PDF pdfN = new PDF();
                pdfN.exportStringToPDF(dateString, text);
            } else {
                warningCN.setText("Text is empty");
            }
        }
    }

    @FXML
    void clear(ActionEvent event) {
        clearWarnings();
        titleBox.setValue(null); // stops showing the current title
        titleN.clear();
        clearPage();
    }
    public void clearWarnings(){
        warningN.setText("");
        warningCN.setText("");
    }
    @FXML
    void loadPage() {
        File f = new File("src\\main\\resources\\index.html");
        engine.load(f.toURI().toString());
    }

    @FXML
    void setPage(String html){
        clearPage();
        engine.executeScript("set('"+html+"')");

    }
    @FXML
    String getPage(){
        String html = (String)engine.executeScript("get()");
        return html;
    }

    @FXML
    void clearPage(){
        engine.executeScript("$('#summernote').summernote('reset');");
    }


    @FXML
    void searchCal(ActionEvent event) {
        List<String> selectedItemsCopy = new ArrayList<String>(listView.getSelectionModel().getSelectedItems());
        listView.getItems().removeAll(selectedItemsCopy);
        listView.getItems().clear();
        searchResult.clear();

        String searchText = searchBar.getText();
        for(Map.Entry pair: calendarNoteList.entrySet()){
            if(calendarNoteList.get(pair.getKey()).get(0) != null){
                if(calendarNoteList.get(pair.getKey()).get(0).contains(searchText)){
                    listView.getItems().add(pair.getKey().toString());
                }
            }
        }
    }

}
