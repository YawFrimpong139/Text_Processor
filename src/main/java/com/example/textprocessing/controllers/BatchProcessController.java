package com.example.textprocessing.controllers;


import com.example.textprocessing.models.textprocessing.TextProcessor;
import com.example.textprocessing.models.textprocessing.FileOperations;
import com.example.textprocessing.AlertUtils;
import com.example.textprocessing.models.textprocessing.FileOperations;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BatchProcessController {
    @FXML private TextField inputDirField;
    @FXML private TextField outputDirField;
    @FXML private TextField filePatternField;
    @FXML private TextField regexField;
    @FXML private TextField replaceField;
    @FXML private CheckBox recursiveCheckBox;
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;

    private Stage primaryStage;
    private FileOperations fileOperations;
    private String encoding;
    private int regexFlags;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setFileProcessor(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    public void setRegexPattern(String pattern) {
        regexField.setText(pattern);
    }

    public void setReplacementText(String replacement) {
        replaceField.setText(replacement);
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setRegexFlags(int flags) {
        this.regexFlags = flags;
    }

    @FXML
    private void handleInputDirBrowse() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(primaryStage);
        if (dir != null) {
            inputDirField.setText(dir.getAbsolutePath());
        }
    }

    @FXML
    private void handleOutputDirBrowse() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(primaryStage);
        if (dir != null) {
            outputDirField.setText(dir.getAbsolutePath());
        }
    }

    @FXML
    private void handleCancel() {
        // Get the current stage (window) from any control
        Stage stage = (Stage) inputDirField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleProcess() {
        String inputDir = inputDirField.getText();
        String outputDir = outputDirField.getText();
        String filePattern = filePatternField.getText();
        String regex = regexField.getText();
        String replacement = replaceField.getText();
        boolean recursive = recursiveCheckBox.isSelected();

        if (inputDir.isEmpty() || outputDir.isEmpty() || regex.isEmpty()) {
            AlertUtils.showError("Input Error", "Please fill all required fields.");
            return;
        }

        new Thread(() -> {
            try {
                updateUI(() -> {
                    progressBar.setVisible(true);
                    statusLabel.setText("Processing files...");
                });

                File inputDirectory = new File(inputDir);
                File outputDirectory = new File(outputDir);

                if (!outputDirectory.exists()) {
                    outputDirectory.mkdirs();
                }

                List<File> files = fileOperations.findFiles(inputDirectory, filePattern, recursive);
                int totalFiles = files.size();
                int processed = 0;

                for (File file : files) {
                    String relativePath = inputDirectory.toURI().relativize(file.toURI()).getPath();
                    File outputFile = new File(outputDirectory, relativePath);

                    outputFile.getParentFile().mkdirs();

                    fileOperations.processSingleFile(file, outputFile, regex, replacement,
                            regexFlags, encoding);

                    processed++;
                    final int currentProcessed = processed;
                    updateUI(() -> {
                        progressBar.setProgress((double) currentProcessed / totalFiles);
                        statusLabel.setText(String.format("Processed %d of %d files",
                                currentProcessed, totalFiles));
                    });
                }

                updateUI(() -> {
                    statusLabel.setText(String.format("Successfully processed %d files", totalFiles));
                });
            } catch (Exception e) {
                updateUI(() -> {
                    AlertUtils.showError("Batch Processing Error", "An error occurred: " + e.getMessage());
                    statusLabel.setText("Error during batch processing");
                });
            } finally {
                updateUI(() -> progressBar.setVisible(false));
            }
        }).start();
    }

    private void updateUI(Runnable runnable) {
        javafx.application.Platform.runLater(runnable);
    }
}

