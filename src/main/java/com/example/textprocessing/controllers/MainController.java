package com.example.textprocessing.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    private TextArea ResultsField;

    @FXML
    private Button SearchButton;

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
