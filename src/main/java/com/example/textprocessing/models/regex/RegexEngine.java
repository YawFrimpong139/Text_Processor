package com.example.textprocessing.models.regex;


import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class RegexEngine {
    private Pattern pattern;
    private Matcher matcher;

    public void compilePattern(String regex, int flags) {
        this.pattern = Pattern.compile(regex, flags);
    }

    public RegexResults executeFind(String text) {
        this.matcher = pattern.matcher(text);
        List<RegexMatch> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(new RegexMatch(
                    matcher.group(),
                    matcher.start(),
                    matcher.end(),
                    getGroups()
            ));
        }

        return new RegexResults(pattern.toString(), matches);
    }

    public String executeReplace(String text, String replacement) {
        this.matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }

    private List<String> getGroups() {
        List<String> groups = new ArrayList<>();
        if (matcher.groupCount() > 0) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                groups.add(matcher.group(i));
            }
        }
        return groups;
    }

    public static class RegexMatch {
        private final String match;
        private final int start;
        private final int end;
        private final List<String> groups;

        public RegexMatch(String match, int start, int end, List<String> groups) {
            this.match = match;
            this.start = start;
            this.end = end;
            this.groups = groups;
        }

        // Getters
        public String getMatch() { return match; }
        public int getStart() { return start; }
        public int getEnd() { return end; }
        public List<String> getGroups() { return groups; }
    }

}

