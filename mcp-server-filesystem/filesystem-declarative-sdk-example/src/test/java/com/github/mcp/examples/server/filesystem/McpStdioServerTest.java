package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.server.McpServerInfo;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class McpStdioServerTest {

    static {
        // Enable Byte Buddy experimental support for Java 21
        System.setProperty("net.bytebuddy.experimental", "true");
    }

    @Test
    void mainInvokesStdioServerRun() {
        try (MockedStatic<McpServers> ms = mockStatic(McpServers.class)) {
            com.github.codeboyzhou.mcp.declarative.McpServers fake = mock(com.github.codeboyzhou.mcp.declarative.McpServers.class);
            ms.when(() -> McpServers.run(McpStdioServer.class, new String[0])).thenReturn(fake);
            assertDoesNotThrow(() -> McpStdioServer.main(new String[0]));
            verify(fake).startSyncStdioServer(any(McpServerInfo.class));
        }
    }
}
