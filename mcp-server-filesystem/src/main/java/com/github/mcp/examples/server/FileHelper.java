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

        private record Permission(List<String> readable, List<String> writable) {

        }

        public static boolean checkReadableConfiguration(Path filepath) {
            final String permissionConfig = System.getenv().get("permission");
            if (permissionConfig == null || permissionConfig.isBlank()) {
                System.err.println("WARNING: No permission configurations detected, all files readable by default.");
                return true;
            }

            Permission permission;
            try {
                permission = json.readValue(permissionConfig, Permission.class);
            } catch (JsonProcessingException e) {
                System.err.println("Invalid permission configuration: " + permissionConfig);
                e.printStackTrace(System.err);
                return false;
            }

            if (permission.readable == null) {
                System.err.println("WARNING: No read permission configurations detected, all files readable by default.");
                return true;
            }

            return checkPermissions(filepath.toString(), permission.readable);
        }

        private static boolean checkPermissions(String filepath, List<String> permissions) {
            for (String regex : permissions) {
                try {
                    if (Pattern.compile(regex).matcher(filepath).matches()) {
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    // Skip invalid regex patterns
                    System.err.println("Skipping invalid regex pattern: " + regex);
                    e.printStackTrace(System.err);
                }
            }

            // Default deny - if no rules match, access is denied
            return false;
        }

    }

}
