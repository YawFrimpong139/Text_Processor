package com.example.textprocessing.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WordFrequencyController {
    @FXML private TableView<WordFrequency> wordFrequencyTable;
    @FXML private TableColumn<WordFrequency, String> wordColumn;
    @FXML private TableColumn<WordFrequency, Integer> countColumn;

    public void setWordFrequencyData(Map<String, Integer> wordFrequencyMap) {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        List<WordFrequency> data = wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        wordFrequencyTable.getItems().setAll(data);
    }

    public static class WordFrequency {
        private final String word;
        private final int count;

        public WordFrequency(String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() { return word; }
        public int getCount() { return count; }
    }
}

