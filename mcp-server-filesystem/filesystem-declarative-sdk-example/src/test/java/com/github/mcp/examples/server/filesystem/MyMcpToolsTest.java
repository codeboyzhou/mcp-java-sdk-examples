package com.github.mcp.examples.server.filesystem;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MyMcpToolsTest {

    @Test
    void readFileNonexistent() throws IOException {
        MyMcpTools tools = new MyMcpTools();
        String path = "nonexistent.txt";
        String result = tools.readFile(path);
        assertTrue(result.contains("does not exist"), "Should report missing file");
    }

    @Test
    void listFilesNonexistent() throws IOException {
        MyMcpTools tools = new MyMcpTools();
        String path = "no_such_dir";
        String result = tools.listFiles(path, "", false);
        assertTrue(result.contains("does not exist"), "Should report missing directory");
    }

    @Test
    void readFileAndListOnTemp() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "hello");
        MyMcpTools tools = new MyMcpTools();
        String content = tools.readFile(tempFile.toString());
        assertEquals("hello", content);
        Files.delete(tempFile);
    }
}
