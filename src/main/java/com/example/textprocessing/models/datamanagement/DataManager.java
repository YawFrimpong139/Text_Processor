package com.example.textprocessing.models.datamanagement;


import java.util.*;
import java.util.stream.*;

public class DataManager {
    private final Map<String, WordData> wordData = new HashMap<>();
    private final List<TextItem> processedItems = new ArrayList<>();

    public void processText(String text) {
        Arrays.stream(text.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .forEach(word -> {
                    wordData.computeIfAbsent(word.toLowerCase(), WordData::new)
                            .incrementCount();
                });

        processedItems.add(new TextItem(text));
    }

    public List<WordData> getSortedWordData() {
        return wordData.values().stream()
                .sorted(Comparator.comparingInt(WordData::getCount).reversed())
                .collect(Collectors.toList());
    }

    public List<TextItem> getProcessedItems() {
        return new ArrayList<>(processedItems);
    }

    public void clearAllData() {
        wordData.clear();
        processedItems.clear();
    }

    public Map<String, Integer> getWordFrequencyMap() {
        return wordData.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getCount()
                ));
    }
}


