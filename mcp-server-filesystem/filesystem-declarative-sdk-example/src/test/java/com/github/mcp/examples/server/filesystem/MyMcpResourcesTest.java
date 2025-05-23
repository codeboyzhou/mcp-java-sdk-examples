package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.annotation.McpResource;
import com.github.codeboyzhou.mcp.declarative.annotation.McpResources;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class MyMcpResourcesTest {

    @Test
    void classIsAnnotatedWithMcpResources() {
        assertTrue(MyMcpResources.class.isAnnotationPresent(McpResources.class),
                   "MyMcpResources should be annotated with @McpResources");
    }

    @Test
    void filesystemMethodHasMcpResourceAnnotation() throws NoSuchMethodException {
        Method m = MyMcpResources.class.getMethod("filesystem");
        assertTrue(m.isAnnotationPresent(McpResource.class), "filesystem() should have @McpResource");
        McpResource ann = m.getAnnotation(McpResource.class);
        assertEquals("file://system", ann.uri(), "URI should match");
        assertEquals("filesystem", ann.name(), "name should match");
        assertEquals("File system operations interface", ann.description(), "description should match");
    }
}
