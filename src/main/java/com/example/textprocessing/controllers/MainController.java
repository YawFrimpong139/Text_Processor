package com.example.textprocessing.controllers;

import com.example.textprocessing.models.textprocessing.TextProcessor;
import com.example.textprocessing.models.textprocessing.FileOperations;
import com.example.textprocessing.models.datamanagement.DataManager;
import com.example.textprocessing.AlertUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;


public class MainController {
    @FXML private TextArea inputTextArea;
    @FXML private TextArea outputTextArea;
    @FXML private TextField regexPatternField;
    @FXML private TextField replacementField;
    @FXML private ComboBox<String> operationComboBox;
    @FXML private ComboBox<String> fileEncodingComboBox;
    @FXML private CheckBox caseSensitiveCheckBox;
    @FXML private CheckBox multilineCheckBox;
    @FXML private CheckBox dotAllCheckBox;
    @FXML private Label statusLabel;

    private final TextProcessor textProcessor = new TextProcessor();
    private final DataManager dataManager = new DataManager();
    private final FileOperations fileProcessor = new FileOperations();
    private Stage primaryStage;

    private static final String[] OPERATIONS = {
            "Find Matches", "Replace All", "Extract Matches", "Word Frequency"
    };

    private static final String[] ENCODINGS = {
            "UTF-8", "ISO-8859-1", "Windows-1252", "UTF-16"
    };

    @FXML
    public void initialize() {
        operationComboBox.getItems().addAll(OPERATIONS);
        operationComboBox.setValue(OPERATIONS[0]);

        fileEncodingComboBox.getItems().addAll(ENCODINGS);
        fileEncodingComboBox.setValue(ENCODINGS[0]);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleExecute() {
        String text = inputTextArea.getText();
        String pattern = regexPatternField.getText();
        String operation = operationComboBox.getValue();

        if (text.isEmpty()) {
            AlertUtils.showError("Input Error", "Please enter text or load a file first.");
            return;
        }

        if ((operation.equals("Find Matches") || operation.equals("Replace All") ||
                operation.equals("Extract Matches")) && pattern.isEmpty()) {
            AlertUtils.showError("Pattern Error", "Please enter a regex pattern for this operation.");
            return;
        }

        try {
            int flags = getRegexFlags();
            String result = "";

            switch (operation) {
                case "Find Matches":
                    result = textProcessor.executeRegexOperation(
                            text, pattern, flags, TextProcessor.RegexOperation.FIND
                    );
                    break;

                case "Replace All":
                    result = textProcessor.executeRegexOperation(
                            text, pattern, flags,
                            TextProcessor.RegexOperation.REPLACE.withReplacement(replacementField.getText())
                    );
                    dataManager.processText(result);
                    break;

                case "Extract Matches":
                    result = textProcessor.executeRegexOperation(
                            text, pattern, flags, TextProcessor.RegexOperation.EXTRACT
                    );
                    dataManager.processText(result);
                    break;

                case "Word Frequency":
                    dataManager.processText(text);
                    result = dataManager.getSortedWordData().stream()
                            .map(w -> w.getWord() + ": " + w.getCount())
                            .collect(Collectors.joining("\n"));
                    break;
            }

            outputTextArea.setText(result);
            statusLabel.setText("Operation completed successfully");
        } catch (PatternSyntaxException e) {
            AlertUtils.showError("Regex Error", "Invalid regular expression pattern: " + e.getMessage());
            statusLabel.setText("Error in regex pattern");
        } catch (Exception e) {
            AlertUtils.showError("Processing Error", "An error occurred during processing: " + e.getMessage());
            statusLabel.setText("Error during processing");
        }
    }

    @FXML
    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.csv", "*.log", "*.xml", "*.json"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                String encoding = fileEncodingComboBox.getValue();
                String content = fileProcessor.readFile(selectedFile, encoding);
                inputTextArea.setText(content);
                statusLabel.setText("File loaded: " + selectedFile.getName());
            } catch (IOException e) {
                AlertUtils.showError("File Error", "Could not read file: " + e.getMessage());
                statusLabel.setText("Error loading file");
            }
        }
    }

    @FXML
    private void handleSaveOutput() {
        if (outputTextArea.getText().isEmpty()) {
            AlertUtils.showError("Save Error", "No output to save.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        if (selectedFile != null) {
            try {
                fileProcessor.saveFile(selectedFile, outputTextArea.getText(),
                        fileEncodingComboBox.getValue());
                statusLabel.setText("Output saved to: " + selectedFile.getName());
            } catch (IOException e) {
                AlertUtils.showError("Save Error", "Could not save file: " + e.getMessage());
                statusLabel.setText("Error saving file");
            }
        }
    }

    @FXML
    private void handleWordFrequencyAnalysis() {
        if (dataManager.getSortedWordData().isEmpty()) {
            AlertUtils.showError("Analysis Error",
                    "No word frequency data available. Run Word Frequency operation first.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/textprocessing/word_frequency.fxml"));
            Parent root = loader.load();

            WordFrequencyController controller = loader.getController();
            controller.setWordFrequencyData(dataManager.getWordFrequencyMap());

            Stage dialog = new Stage();
            dialog.setTitle("Complete Word Frequency Analysis");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(primaryStage);
            dialog.setScene(new Scene(root));
            dialog.show();
        } catch (IOException e) {
            AlertUtils.showError("Error", "Could not open word frequency dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleBatchProcess() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/textprocessing/batch_process.fxml"));
            Parent root = loader.load();

            BatchProcessController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setFileProcessor(fileProcessor);
            controller.setRegexPattern(regexPatternField.getText());
            controller.setReplacementText(replacementField.getText());
            controller.setEncoding(fileEncodingComboBox.getValue());
            controller.setRegexFlags(getRegexFlags());

            Stage dialog = new Stage();
            dialog.setTitle("Batch Process Files");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(primaryStage);
            dialog.setScene(new Scene(root));
            dialog.show();
        } catch (IOException e) {
            AlertUtils.showError("Error", "Could not open batch process dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegexHelp() {
        String content = "Common Regex Patterns:\n\n" +
                "Literal characters: match themselves\n" +
                "  Example: 'cat' matches \"cat\"\n\n" +
                "Character classes:\n" +
                "  [abc] - a, b, or c\n" +
                "  [^abc] - Any character except a, b, or c\n" +
                "  [a-z] - Any lowercase letter\n" +
                "  [A-Z] - Any uppercase letter\n" +
                "  [0-9] - Any digit\n" +
                "  \\d - Any digit (same as [0-9])\n" +
                "  \\w - Any word character (letter, digit, underscore)\n" +
                "  \\s - Any whitespace character\n\n" +
                "Quantifiers:\n" +
                "  ? - Zero or one\n" +
                "  * - Zero or more\n" +
                "  + - One or more\n" +
                "  {n} - Exactly n times\n" +
                "  {n,} - At least n times\n" +
                "  {n,m} - Between n and m times\n\n" +
                "Anchors:\n" +
                "  ^ - Start of line (or string in multiline mode)\n" +
                "  $ - End of line (or string in multiline mode)\n" +
                "  \\b - Word boundary\n\n" +
                "Groups and Alternation:\n" +
                "  (abc) - Capturing group\n" +
                "  (?:abc) - Non-capturing group\n" +
                "  a|b - Match either a or b\n\n" +
                "Escaping special characters with \\: . * + ? ^ $ { } [ ] ( ) | \\";

        AlertUtils.showInformation("Regex Help", "Regular Expression Quick Reference", content);
    }

    @FXML
    private void handleAbout() {
        String content = "Version 1.0\n\n" +
                "This application provides advanced text processing capabilities including:\n" +
                "- Regular expression search and replace\n" +
                "- Batch file processing\n" +
                "- Word frequency analysis\n" +
                "- Efficient handling of large text files\n\n" +
                "Developed for DataFlow Solutions to automate text processing workflows.";

        AlertUtils.showInformation("About", "DataFlow Solutions - Text Processing System", content);
    }

    @FXML
    private void handleClearOutput() {
        outputTextArea.clear();
        statusLabel.setText("Output cleared");
    }

    @FXML
    private void handleClearAll() {
        inputTextArea.clear();
        outputTextArea.clear();
        regexPatternField.clear();
        replacementField.clear();
        dataManager.clearAllData();
        statusLabel.setText("All cleared");
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) inputTextArea.getScene().getWindow();
        stage.close();
    }

    private int getRegexFlags() {
        int flags = 0;
        if (!caseSensitiveCheckBox.isSelected()) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        if (multilineCheckBox.isSelected()) {
            flags |= Pattern.MULTILINE;
        }
        if (dotAllCheckBox.isSelected()) {
            flags |= Pattern.DOTALL;
        }
        return flags;
    }
}