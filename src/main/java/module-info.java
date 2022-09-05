module com.example.calendarnotes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires javafx.web;
    requires html2pdf;

    opens com.example.calendarnotes to javafx.fxml;
    exports com.example.calendarnotes;
}