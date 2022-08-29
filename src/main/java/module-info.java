module com.example.calendarnotes {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calendarnotes to javafx.fxml;
    exports com.example.calendarnotes;
}