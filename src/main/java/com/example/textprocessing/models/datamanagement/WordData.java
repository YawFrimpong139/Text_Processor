package com.example.textprocessing.models.datamanagement;



import java.util.Objects;

public class WordData {
    private final String word;
    private int count;

    public WordData(String word) {
        this.word = word;
        this.count = 0;
    }

    public void incrementCount() {
        count++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordData wordData = (WordData) o;
        return word.equalsIgnoreCase(wordData.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word.toLowerCase());
    }

    // Getters
    public String getWord() { return word; }
    public int getCount() { return count; }
}
