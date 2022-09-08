package com.example.calendarnotes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("project.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("calendar_buttons.css");
        scene.getStylesheets().add("searchList.css");
        stage.setTitle("NoteMe");
        stage.getIcons().add(new Image(Main.class.getResource("/icon.png").toString()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}