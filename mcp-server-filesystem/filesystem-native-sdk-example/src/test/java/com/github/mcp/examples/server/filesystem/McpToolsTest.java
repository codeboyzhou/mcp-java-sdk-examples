package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import io.modelcontextprotocol.server.McpServer;
import static org.mockito.Mockito.*;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class McpToolsTest {

    @Test
    void readFileSpecHasCorrectTool() throws IOException {
        SyncToolSpecification spec = McpTools.readFile();
        McpSchema.Tool tool = spec.tool();
        assertEquals("read_file", tool.name());
        assertEquals("Read complete contents of a file.", tool.description());
        assertNotNull(tool.inputSchema(), "Tool JSON schema should be present");
    }

    @Test
    void listFilesSpecHasCorrectTool() throws IOException {
        SyncToolSpecification spec = McpTools.listFiles();
        McpSchema.Tool tool = spec.tool();
        assertEquals("list_files", tool.name());
        assertEquals("List files of a directory.", tool.description());
        assertNotNull(tool.inputSchema(), "Tool JSON schema should be present");
    }


    @Test
    void addAllToRegistersToolsNotifies() {
        // Create a mock transport provider
        McpServerTransportProvider mockProvider = mock(McpServerTransportProvider.class);
        // Stub the notification call to return a real (empty) Mono
        when(mockProvider.notifyClients(eq(McpSchema.METHOD_NOTIFICATION_TOOLS_LIST_CHANGED), isNull()))
            .thenReturn(Mono.empty());

        // Build async server with mock transport
        var async = McpServer.async(mockProvider)
            .capabilities(McpSchema.ServerCapabilities.builder()
                .tools(true)
                .build())
            .build();
        McpSyncServer server = new McpSyncServer(async);

        // Invoke tool registration
        McpTools.addAllTo(server);

        // Verify that notifyClients was called twice
        verify(mockProvider, times(2)).notifyClients(eq(McpSchema.METHOD_NOTIFICATION_TOOLS_LIST_CHANGED), isNull());
    }
}
