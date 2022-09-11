package com.example.calendarnotes;

import com.itextpdf.text.*;
import javafx.beans.value.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.time.*;

import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Controller implements Initializable {

    //web stuff
    @FXML
    private WebView webView;

    private WebEngine engine;
    //

    @FXML
    private Tab tabCN,tab_notes;

    @FXML
    private ChoiceBox<Integer> yearBox;

    @FXML
    private ChoiceBox<Month> monthBox;

    @FXML
    private TextArea textCN;
    
    @FXML
    private ColorPicker color;
    
    @FXML
    private Label date_text;

    @FXML
    private  ScrollPane notes_area;
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
    private Label warningN, warningCN; // place to show warnings to user

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField searchBar;

    @FXML
    private TextArea searchResult;

    @FXML
    private GridPane grid;

    Stickey_Notes stickey_notes = new Stickey_Notes();
    //GridPane grid = new GridPane();
    @FXML
    private Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24,b25,b26,b27,b28,b29,b30,b31,b32,b33,b34,b35,b36,b37;
    private List<Button> list = new ArrayList<>();
    Button currentButton = new Button(); // keep track of which day button was clicked last
    String currentTitle = null;

    Month[] monthList = Month.values();
    List<Integer> yearList = IntStream.range(1970, 2023).boxed().collect(Collectors.toList());

    static HashMap<String, List<String>> calendarNoteList = new HashMap<>(); // will making this static cause a problem?
    static DBCalendar DBCalendar = new DBCalendar(); // will making this static cause a problem?


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        notes_area.setContent(stickey_notes.notes_init(grid));
        stickey_notes.setDisp_dab(disp_dab);
        stickey_notes.setNotes_tab(notes_tab);
        stickey_notes.setAdd(add);
        stickey_notes.setEdit(edit);
        stickey_notes.setDelete(delete);
        stickey_notes.setWarningN(warningN);
        stickey_notes.setTitle_field(title_field);

        notes_area.widthProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                stickey_notes.resize(grid, (Double) newValue);
            }
        });

        grid.setPadding(new Insets(5));
        grid.setHgap(5);
        grid.setVgap(5);

//        grid.getStyleClass().add("anchor-pane");

        //web stuff
        engine = webView.getEngine();
        stickey_notes.setEngine(engine);
        loadPage();
        //

        monthBox.getItems().addAll(monthList);
        yearBox.getItems().addAll(yearList);


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

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String selectedDate = listView.getSelectionModel().getSelectedItem();
                if (selectedDate!=null) { // this method gets called when trying to unselect item, so it can be null in that case
                    try {
                        String textResult = calendarNoteList.get(selectedDate).get(0);
                        searchResult.setText(textResult);
                    } catch (Exception e) {
                        searchResult.setText("");
                    }
                }
            }
        });
    }


    private void printCalendarMonthYear(int month, int year, List<Button> butonlist) {
        for (Button button:butonlist) {
            button.setText("");
        }
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(year, month-1, 1);
        int numberOfMonthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstWeekdayOfMonth = firstWeekD(cal);
        printCalendar(numberOfMonthDays, firstWeekdayOfMonth, butonlist);
    }

    public static int firstWeekD (Calendar cal){
        int firstWeekdayOfMonth = cal.get(Calendar.DAY_OF_WEEK)-1;
        firstWeekdayOfMonth = ((firstWeekdayOfMonth == 0) ? 7 : firstWeekdayOfMonth);
        return firstWeekdayOfMonth;
    }

    private void printCalendar(int numberOfMonthDays, int firstWeekdayOfMonth,List<Button> butonlist) {
        int day_of_month = 1;
        DateInfo dateContr = new DateInfo();
        for (int j =0; j<37;j++){
            butonlist.get(j).setStyle("-fx-background-color: #cdcdcd");
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
        for (int i = 5; i < list.size(); i+=7) {
            list.get(i).getStyleClass().add("weekend_red");
            list.get(i+1).getStyleClass().add("weekend_red");
        }
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
            color.setValue(Color.valueOf("#cdcdcd"));
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
        calendarNoteList = calendarCreateOrUpdate(dateString, text, colour, list_of_props);
    }
    public static HashMap<String, List<String>> calendarCreateOrUpdate(String dateString, String text, String colour, List<String> list_of_props){ // for testing
        if(!calendarNoteList.containsKey(dateString)){
            calendarNoteList.put(dateString, list_of_props);
            DBCalendar.insert(dateString,text,colour );
            return calendarNoteList;
        }
        else{
            calendarNoteList.put(dateString, list_of_props);
            DBCalendar.update(dateString,text,colour);
            return calendarNoteList;
        }
    }

    @FXML
    void deleteCalendarNote(ActionEvent event) {
        clearWarnings();
        DateInfo dateContr = new DateInfo();
        String dateString = dateContr.dateFormat(currentButton, monthBox, yearBox);
        currentButton.setStyle("-fx-background-color: #cdcdcd");
        color.setValue(Color.valueOf("#cdcdcd"));
        calendarNoteList.remove(dateString);
        DBCalendar.remove(dateString);
        textCN.clear();
    }
    @FXML
    void back(ActionEvent event){
        disp_dab.setVisible(true);
        notes_tab.setVisible(false);
    }
    @FXML
    private void add() {
        disp_dab.setVisible(false);
        notes_tab.setVisible(true);
        add.setVisible(true);
        edit.setVisible(false);
        delete.setVisible(false);
        title_field.clear();
        clearPage();
    }
    @FXML
    void createNote(ActionEvent event) {

        clearWarnings();
        String title = title_field.getText();
        currentTitle = title;
        if(title != null && !(title.isEmpty())) {
            String text = getPage();
            if (!stickey_notes.noteList.containsKey(title)) {
                disp_dab.setVisible(true);
                notes_tab.setVisible(false);
                stickey_notes.db.insert(title, text);
                stickey_notes.noteList.put(title, text);
                stickey_notes.add(grid,title);
            } else {
                warningN.setText("Title already exists");
            }
        } else {
            warningN.setText("Title is empty");
        }
    }
    @FXML
    void save(ActionEvent event) {
        clearWarnings();
        String title = title_field.getText();
        currentTitle = stickey_notes.getCurrentTitle();
        if(title != null && !(title.isEmpty())) {
            String text = getPage();
            if (!stickey_notes.noteList.containsKey(title)) {
                stickey_notes.db.updateTitle(currentTitle,title);
            }
            stickey_notes.db.update(title, text);
            stickey_notes.noteList.put(title, text);
            stickey_notes.update(title);
        } else {
            warningN.setText("Title is empty");
        }
    }

    @FXML
    void deleteNote(ActionEvent event){
        clearWarnings();
        disp_dab.setVisible(true);
        notes_tab.setVisible(false);
        String title = title_field.getText();
        stickey_notes.db.remove(title);
        stickey_notes.notes_init(grid);
        clearPage();
    }


    @FXML
    void exportToPDF(ActionEvent event) throws DocumentException, IOException {
        clearWarnings();
        if (tab_notes.isSelected()) { // to know from which tab we are exporting (because then the keys are different)
            if (!title_field.getText().isEmpty()) {
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
        engine.executeScript("$('#SummernoteText').summernote('reset');");
    }
    @FXML
    String getPage(){
        String html = (String)engine.executeScript("get()");
        return html;
    }

    @FXML
    void clearPage(){
        engine.executeScript("$('#SummernoteText').summernote('reset');"); // engine.executeScript("$('#SummernoteText').summernote('code', '');");
        engine.executeScript("set('<p><br></p>')");

    }
    @FXML
    void searchCal(ActionEvent event) {
        listView.getItems().clear();
        searchResult.clear();
        List<String> selectedItemsCopy = new ArrayList<>(listView.getSelectionModel().getSelectedItems());
        listView.getItems().removeAll(selectedItemsCopy);

        String searchText = searchBar.getText();
        for(Map.Entry<String, List<String>> pair: calendarNoteList.entrySet()){
            if(calendarNoteList.get(pair.getKey()).get(0) != null){
                if(calendarNoteList.get(pair.getKey()).get(0).toLowerCase().contains(searchText.toLowerCase())){ //not case sensitive
                    listView.getItems().add(pair.getKey());
                }
            }
        }
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    setStyle("-fx-background-color: "+calendarNoteList.get(item).get(1).replace("0x", "#")+";");
                }
            }
        });
    }

}
