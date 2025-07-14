package com.github.mcp.server.filesystem.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class FileOperationHelper {

    public static String readResourceAsString(String filename) throws IOException {
        try (InputStream inputStream = FileOperationHelper.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new NoSuchFileException(filename);
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.lines().collect(joining(System.lineSeparator()));
        }
    }

    public static String readAsString(Path filepath) throws IOException {
        return Files.readAllLines(filepath).stream().collect(joining(System.lineSeparator()));
    }

    public static List<String> listFiles(Path directoryPath, String fileNamePattern, boolean recursive) throws IOException {
        if (recursive) {
            MyFileVisitor visitor = new MyFileVisitor(fileNamePattern);
            Files.walkFileTree(directoryPath, visitor);
            return visitor.getPaths();
        } else {
            try (Stream<Path> stream = Files.list(directoryPath)) {
                Stream<String> names = stream.filter(Files::isRegularFile).map(Path::getFileName).map(Path::toString);
                if (fileNamePattern == null || fileNamePattern.isBlank()) {
                    return names.toList();
                }
                return names.filter(name -> Pattern.compile(fileNamePattern).matcher(name).find()).toList();
            }
        }
    }

}
