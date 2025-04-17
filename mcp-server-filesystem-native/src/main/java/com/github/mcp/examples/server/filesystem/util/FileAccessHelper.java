package com.github.mcp.examples.server.filesystem.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public final class FileAccessHelper {

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
                if (Pattern.compile(regex).matcher(filepath).find()) {
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
