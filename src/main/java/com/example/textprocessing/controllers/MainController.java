package com.example.textprocessing.controllers;

import com.example.textprocessing.models.textprocessing.TextProcessor;
import com.example.textprocessing.models.textprocessing.FileOperations;
import com.example.textprocessing.AlertUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;
import java.nio.file.Files;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;


public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea InsertField;

    @FXML
    private Button LoadFileButton;

    @FXML
    private Label StatusLabel;

    @FXML
    private TextField RegexField;

    @FXML
    private Button RepalceAllButton;

    @FXML
    private TextField ReplaceField;

    @FXML
    private TextArea ResultsField;

    @FXML
    private Button SearchButton;


    @FXML
    private void handleSearch(){
        String text = InsertField.getText();
        String regex = RegexField.getText();


        if(text.isEmpty() || regex.isEmpty()){
            StatusLabel.setText("Error: Input text and Regular Expression Required ");
            return;
        }

        try{
            ResultsField.setText(String.join("\n", RegPattern.findAllMatches(text, regex)));
            StatusLabel.setText("Search Completed!!");
        }catch(Exception e){
            StatusLabel.setText("Error: " + e.getMessage());
        }
    }


    @FXML
    private void handleReplace(){
        String text = InsertField.getText();
        String regex = RegexField.getText();
        String replacement = ReplaceField.getText();


        if(text.isEmpty() || regex.isEmpty() || replacement.isEmpty()){
            StatusLabel.setText("Error: Kindly check your inputs");
            return;
        }

        try{
            ResultsField.setText(RegPattern.replaceAll(text, regex, replacement));
            StatusLabel.setText("Replacement Completed!!");
        }catch(Exception e){
            StatusLabel.setText("Error: " + e.getMessage());
        }


    }

    @FXML
    private void handleLoadFile(){
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Text file");
        File file = fileChooser.showOpenDialog(InsertField.getScene().getWindow());

        if(file != null){
            try{
                InsertField.setText(Files.readString(file.toPath()));
                StatusLabel.setText("Loaded: " + file.getName());
            }catch(Exception e){
                StatusLabel.setText("Error loading file: " + e.getMessage());
            }
        }



    }

    @FXML
    void initialize() {
        assert InsertField != null : "fx:id=\"InsertField\" was not injected: check your FXML file 'main.fxml'.";
        assert LoadFileButton != null : "fx:id=\"LoadFileButton\" was not injected: check your FXML file 'main.fxml'.";
        assert StatusLabel != null : "fx:id=\"StatusLabel\" was not injected: check your FXML file 'main.fxml'.";
        assert RegexField != null : "fx:id=\"RegexField\" was not injected: check your FXML file 'main.fxml'.";
        assert RepalceAllButton != null : "fx:id=\"RepalceAllButton\" was not injected: check your FXML file 'main.fxml'.";
        assert ResultsField != null : "fx:id=\"ResultsField\" was not injected: check your FXML file 'main.fxml'.";
        assert SearchButton != null : "fx:id=\"SearchButton\" was not injected: check your FXML file 'main.fxml'.";

    }



}
