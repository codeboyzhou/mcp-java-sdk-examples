package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.server.McpSseServerInfo;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class McpSseServerTest {

    static {
        // Enable Byte Buddy experimental support for Java 21
        System.setProperty("net.bytebuddy.experimental", "true");
    }

    @Test
    void mainInvokesServerRun() {
        try (MockedStatic<McpServers> ms = mockStatic(McpServers.class)) {
            com.github.codeboyzhou.mcp.declarative.McpServers fake = mock(com.github.codeboyzhou.mcp.declarative.McpServers.class);
            ms.when(() -> McpServers.run(McpSseServer.class, new String[0])).thenReturn(fake);
            assertDoesNotThrow(() -> McpSseServer.main(new String[0]));
            verify(fake).startSyncSseServer(any(McpSseServerInfo.class));
        }
    }
}
