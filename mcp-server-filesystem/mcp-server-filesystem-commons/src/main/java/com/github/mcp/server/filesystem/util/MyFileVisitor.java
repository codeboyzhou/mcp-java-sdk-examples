package com.github.mcp.server.filesystem.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class MyFileVisitor implements FileVisitor<Path> {

    private final String fileNamePattern;

    private final List<String> paths = new ArrayList<>();

    public MyFileVisitor(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public List<String> getPaths() {
        return paths;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if (Files.isReadable(dir)) {
            return FileVisitResult.CONTINUE;
        }
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (fileNamePattern == null || fileNamePattern.isBlank()) {
            paths.add(file.toAbsolutePath().toString());
            return FileVisitResult.CONTINUE;
        }

        if (Pattern.compile(fileNamePattern).matcher(file.getFileName().toString()).find()) {
            paths.add(file.toAbsolutePath().toString());
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

}
