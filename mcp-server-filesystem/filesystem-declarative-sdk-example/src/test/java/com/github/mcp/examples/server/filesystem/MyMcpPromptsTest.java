package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompt;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompts;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

class MyMcpPromptsTest {

    @Test
    void classIsAnnotatedWithMcpPrompts() {
        assertTrue(MyMcpPrompts.class.isAnnotationPresent(McpPrompts.class),
                   "MyMcpPrompts should be annotated with @McpPrompts");
    }

    @Test
    void readFileMethodAnnotation() throws NoSuchMethodException {
        Method m = MyMcpPrompts.class.getMethod("readFile", String.class);
        McpPrompt ann = m.getAnnotation(McpPrompt.class);
        assertNotNull(ann, "readFile should have @McpPrompt");
        assertEquals("read_file", ann.name());
        assertEquals("Read complete contents of a file.", ann.description());
        // check parameter annotation
        Parameter p = m.getParameters()[0];
        assertTrue(p.isAnnotationPresent(com.github.codeboyzhou.mcp.declarative.annotation.McpPromptParam.class));
    }

    @Test
    void listFilesMethodAnnotation() throws NoSuchMethodException {
        Method m = MyMcpPrompts.class.getMethod("listFiles", String.class, String.class, boolean.class);
        McpPrompt ann = m.getAnnotation(McpPrompt.class);
        assertNotNull(ann, "listFiles should have @McpPrompt");
        assertEquals("list_files", ann.name());
        assertEquals("List files of a directory.", ann.description());
        assertEquals(3, m.getParameters().length);
    }
}
