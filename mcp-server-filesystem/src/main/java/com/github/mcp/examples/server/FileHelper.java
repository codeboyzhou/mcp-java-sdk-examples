package com.github.mcp.examples.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static java.util.stream.Collectors.joining;

public final class FileHelper {

    public static String readResourceAsString(String filename) throws IOException {
        try (InputStream inputStream = FileHelper.class.getClassLoader().getResourceAsStream(filename)) {
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

}
