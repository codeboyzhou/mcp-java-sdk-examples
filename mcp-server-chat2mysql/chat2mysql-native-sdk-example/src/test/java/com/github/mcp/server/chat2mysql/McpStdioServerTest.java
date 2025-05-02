
package com.github.mcp.server.chat2mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import io.modelcontextprotocol.server.McpSyncServer;

class McpStdioServerTest {

    private McpStdioServer stdioServer;

    @BeforeEach
    void setUp() {
        stdioServer = new McpStdioServer();
    }

    @Test
    void initializeShouldSetServerField() throws Exception {
        // call private initialize()
        Method init = McpStdioServer.class.getDeclaredMethod("initialize");
        init.setAccessible(true);
        init.invoke(stdioServer);

        // reflectively get 'server' field
        Field field = McpStdioServer.class.getDeclaredField("server");
        field.setAccessible(true);
        Object serverInstance = field.get(stdioServer);

        assertNotNull(serverInstance, "server field should be initialized");
        assertTrue(serverInstance instanceof McpSyncServer,
            "server field should be an instance of McpSyncServer");
    }

    @Test
    void mainShouldCallPromptsAddAllTo() {
        // mock McpPrompts static
        try (MockedStatic<McpPrompts> prompts = mockStatic(McpPrompts.class)) {
            // ensure no exception
            assertDoesNotThrow(() -> McpStdioServer.main(new String[0]));

            // verify addAllTo was called with any McpSyncServer
            prompts.verify(() -> McpPrompts.addAllTo(any(McpSyncServer.class)), times(1));
        }
    }
}
