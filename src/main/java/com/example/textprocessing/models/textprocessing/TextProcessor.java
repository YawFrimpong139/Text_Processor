package com.example.textprocessing.models.textprocessing;


import com.example.textprocessing.models.regex.RegexEngine;
import java.util.*;
import java.util.stream.*;

public class TextProcessor {
    private final FileOperations fileOperations = new FileOperations();

    public String processText(String text, ProcessingOperation operation) {
        return operation.apply(text);
    }

    public List<String> batchProcess(List<String> texts, ProcessingOperation operation) {
        return texts.stream()
                .map(operation::apply)
                .collect(Collectors.toList());
    }

    public String analyzeWordFrequency(String text) {
        Map<String, Long> frequency = Arrays.stream(text.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(
                        String::toLowerCase,
                        Collectors.counting()
                ));

        return frequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    public String executeRegexOperation(String text, String pattern, int flags,
                                        RegexOperation operation) {
        RegexEngine engine = new RegexEngine();
        engine.compilePattern(pattern, flags);

        return switch (operation) {
            case FIND -> engine.executeFind(text).getFormattedResults();
            case REPLACE -> engine.executeReplace(text, operation.getReplacement());
            case EXTRACT -> engine.executeFind(text).getMatches().stream()
                    .map(RegexEngine.RegexMatch::getMatch)
                    .collect(Collectors.joining("\n"));
        };
    }

    public enum RegexOperation {
        FIND, REPLACE, EXTRACT;

        private String replacement;

        public String getReplacement() {
            return replacement;
        }

        public RegexOperation withReplacement(String replacement) {
            this.replacement = replacement;
            return this;
        }
    }

    @FunctionalInterface
    public interface ProcessingOperation {
        String apply(String text);
    }
}
