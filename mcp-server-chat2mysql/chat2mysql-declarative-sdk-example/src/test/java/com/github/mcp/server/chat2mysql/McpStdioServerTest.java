package com.github.mcp.server.chat2mysql;

import com.github.codeboyzhou.mcp.declarative.annotation.McpComponentScan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class McpStdioServerTest {

    @Test
    void classIsAnnotatedWithMcpComponentScan() {
        Class<McpStdioServer> cls = McpStdioServer.class;
        assertTrue(cls.isAnnotationPresent(McpComponentScan.class),
            "McpStdioServer should be annotated with @McpComponentScan");

        McpComponentScan ann = cls.getAnnotation(McpComponentScan.class);
        assertEquals(McpStdioServer.class, ann.basePackageClass(),
            "@McpComponentScan.basePackageClass should point to McpStdioServer.class");
    }

    @Test
    void mainHandlesNoArgsWithoutException() {
        // ensure main method does not throw when called with empty args
        assertDoesNotThrow(() -> McpStdioServer.main(new String[0]),
            "Calling main with no arguments should not throw an exception");
    }
}
