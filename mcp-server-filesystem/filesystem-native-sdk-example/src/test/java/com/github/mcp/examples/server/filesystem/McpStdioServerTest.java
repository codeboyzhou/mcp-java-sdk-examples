
package com.github.mcp.examples.server.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class McpStdioServerTest {

    private McpStdioServer serverInstance;

    @BeforeEach
    void setup() {
        serverInstance = new McpStdioServer();
    }

    @Test
    void initializeShouldSetServerField() throws Exception {
        // call private initialize()
        Method init = McpStdioServer.class.getDeclaredMethod("initialize");
        init.setAccessible(true);
        init.invoke(serverInstance);

        // verify the 'server' field is non-null
        Field field = McpStdioServer.class.getDeclaredField("server");
        field.setAccessible(true);
        Object srv = field.get(serverInstance);
        assertNotNull(srv, "server field should be initialized");
    }

    @Test
    void mainShouldInvokeInitializeAndNotThrow() {
        assertDoesNotThrow(() -> McpStdioServer.main(new String[0]),
            "main() should complete without throwing");
    }
}
