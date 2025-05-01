package com.example.textprocessing.models.textprocessing;


import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;

public class FileOperations {
    public String readFile(File file, String encoding) throws IOException {
        return Files.readString(file.toPath(), java.nio.charset.Charset.forName(encoding));
    }

    public void saveFile(File file, String content, String encoding) throws IOException {
        Files.writeString(file.toPath(), content, java.nio.charset.Charset.forName(encoding));
    }

    public List<File> findFiles(File directory, String pattern, boolean recursive) throws IOException {
        try (Stream<Path> paths = Files.walk(directory.toPath(),
                recursive ? Integer.MAX_VALUE : 1)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> pattern.isEmpty() ||
                            path.getFileName().toString().matches(convertGlobToRegex(pattern)))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
    }

    public void processSingleFile(File inputFile, File outputFile, String regex,
                                  String replacement, int regexFlags, String encoding) throws IOException {
        String content = readFile(inputFile, encoding);

        Pattern pattern = Pattern.compile(regex, regexFlags);
        String updatedContent = pattern.matcher(content).replaceAll(replacement);

        saveFile(outputFile, updatedContent, encoding);
    }


    private String convertGlobToRegex(String pattern) {
        StringBuilder sb = new StringBuilder(pattern.length());
        for (char c : pattern.toCharArray()) {
            switch (c) {
                case '*' -> sb.append(".*");
                case '?' -> sb.append('.');
                case '.' -> sb.append("\\.");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }
}


