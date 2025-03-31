package com.github.mcp.examples.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

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

    public static final class AccessControl {

        private static final ObjectMapper json = new ObjectMapper();

        private record Access(Read read, Write write) {

        }

        private record Read(List<String> allowed, List<String> denied) {

        }

        private record Write(List<String> allowed, List<String> denied) {

        }

        public static boolean checkReadAccessConfiguration(Path filepath) {
            final String fileAccessConfiguration = System.getenv().get("access");
            if (fileAccessConfiguration == null || fileAccessConfiguration.isBlank()) {
                return false;
            }

            try {
                Access access = json.readValue(fileAccessConfiguration, Access.class);
                if (access.read == null) {
                    return false;
                }
                if (access.read.allowed == null && access.read.denied == null) {
                    return false;
                }

                if (access.read.allowed != null) {
                    for (String regex : access.read.allowed) {
                        return Pattern.compile(regex).matcher(filepath.toString()).matches();
                    }
                }

                if (access.read.denied != null) {
                    for (String regex : access.read.denied) {
                        return !Pattern.compile(regex).matcher(filepath.toString()).matches();
                    }
                }
            } catch (JsonProcessingException e) {
                return false;
            }

            return false;
        }

    }

}
