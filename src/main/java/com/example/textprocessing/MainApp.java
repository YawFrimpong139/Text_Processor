package com.example.textprocessing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        URL fxmlUrl = getClass().getResource("/com/example/textprocessing/main.fxml");

        System.out.println("FXML URL: " + fxmlUrl);

        if (fxmlUrl == null) {
            throw new RuntimeException("FXML file not found!");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);



        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 700);
        // Load CSS safely
        try {
            URL cssUrl = getClass().getResource("/com/example/textprocessing/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("CSS file not found, continuing without styles");
            }
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
        }

        primaryStage.setTitle("DataFlow Solutions - Text Processing System");
        primaryStage.setScene(scene);
        primaryStage.show();





    }

    public static void main(String[] args) {
        launch(args);
    }
}