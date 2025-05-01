package com.example.textprocessing.models.regex;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexResults {
    private final List<RegexEngine.RegexMatch> matches;
    private final String pattern;

    public RegexResults(String pattern, List<RegexEngine.RegexMatch> matches) {
        this.pattern = pattern;
        this.matches = matches;
    }

    public boolean hasMatches() {
        return !matches.isEmpty();
    }

    public List<RegexEngine.RegexMatch> getMatches() {
        return matches;
    }

    public String getFormattedResults() {
        StringBuilder results = new StringBuilder();
        results.append("=== Match Results ===\n");
        results.append("Pattern: ").append(pattern).append("\n\n");

        if (matches.isEmpty()) {
            results.append("No matches found.\n");
            return results.toString();
        }

        for (int i = 0; i < matches.size(); i++) {
            RegexEngine.RegexMatch m = matches.get(i);
            results.append("Match #").append(i + 1).append(":\n");
            results.append("Full match: ").append(m.getMatch()).append("\n");

            if (!m.getGroups().isEmpty()) {
                results.append("Groups:\n");
                for (int j = 0; j < m.getGroups().size(); j++) {
                    results.append("  Group ").append(j + 1).append(": ")
                            .append(m.getGroups().get(j)).append("\n");
                }
            }

            results.append("Start position: ").append(m.getStart()).append("\n");
            results.append("End position: ").append(m.getEnd()).append("\n\n");
        }

        results.append("Total matches found: ").append(matches.size()).append("\n");
        return results.toString();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < matches.size(); i++) {
//            RegexEngine.RegexMatch m = matches.get(i);
//            sb.append("Match ").append(i + 1).append(": ")
//                    .append(m.getMatch()).append("\n")
//                    .append("Position: ").append(m.getStart()).append("-")
//                    .append(m.getEnd()).append("\n");
//
//            if (!m.getGroups().isEmpty()) {
//                sb.append("Groups:\n");
//                for (int j = 0; j < m.getGroups().size(); j++) {
//                    sb.append("  ").append(j + 1).append(": ")
//                            .append(m.getGroups().get(j)).append("\n");
//                }
//            }
//            sb.append("\n");
//        }
//        return sb.toString();
    }
}
