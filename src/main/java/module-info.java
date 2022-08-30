module com.example.calendarnotes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.calendarnotes to javafx.fxml;
    exports com.example.calendarnotes;
}