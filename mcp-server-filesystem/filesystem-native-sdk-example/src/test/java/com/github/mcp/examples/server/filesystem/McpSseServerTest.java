package com.github.mcp.examples.server.filesystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class McpSseServerTest {

    private McpSseServer serverInstance;

    @BeforeEach
    void setup() {
        serverInstance = new McpSseServer();
    }

    @Test
    void initializeShouldSetServerField() throws Exception {
        // call private initialize()
        var initMethod = McpSseServer.class.getDeclaredMethod("initialize");
        initMethod.setAccessible(true);
        initMethod.invoke(serverInstance);

        // verify the 'server' field is non-null
        Field serverField = McpSseServer.class.getDeclaredField("server");
        serverField.setAccessible(true);
        Object srv = serverField.get(serverInstance);
        assertNotNull(srv, "McpSyncServer should be initialized");
    }

    @Test
    void mainShouldNotThrow() {
        ExecutorService exec = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        Future<?> future = exec.submit(() -> McpSseServer.main(new String[0]));
        try {
            future.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // main() did not complete in time; cancel the task
            future.cancel(true);
        } catch (Exception e) {
            fail("main() threw an exception: " + e.getMessage());
        } finally {
            exec.shutdownNow();
        }
        // If we reach here, main() started and was interrupted as expected
        assertTrue(true);
    }
}
