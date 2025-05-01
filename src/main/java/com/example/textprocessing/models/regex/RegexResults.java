package com.example.textprocessing.models.regex;


import java.util.List;

public class RegexResults {
    private final List<RegexEngine.RegexMatch> matches;

    public RegexResults(List<RegexEngine.RegexMatch> matches) {
        this.matches = matches;
    }

    public boolean hasMatches() {
        return !matches.isEmpty();
    }

    public List<RegexEngine.RegexMatch> getMatches() {
        return matches;
    }

    public String getFormattedResults() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matches.size(); i++) {
            RegexEngine.RegexMatch m = matches.get(i);
            sb.append("Match ").append(i + 1).append(": ")
                    .append(m.getMatch()).append("\n")
                    .append("Position: ").append(m.getStart()).append("-")
                    .append(m.getEnd()).append("\n");

            if (!m.getGroups().isEmpty()) {
                sb.append("Groups:\n");
                for (int j = 0; j < m.getGroups().size(); j++) {
                    sb.append("  ").append(j + 1).append(": ")
                            .append(m.getGroups().get(j)).append("\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
