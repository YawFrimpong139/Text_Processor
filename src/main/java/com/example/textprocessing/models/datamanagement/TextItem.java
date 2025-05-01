package com.example.textprocessing.models.datamanagement;

import java.util.Objects;

public class TextItem {
    private final String content;
    private final long timestamp;

    public TextItem(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextItem textItem = (TextItem) o;
        return content.equals(textItem.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    // Getters
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
}

