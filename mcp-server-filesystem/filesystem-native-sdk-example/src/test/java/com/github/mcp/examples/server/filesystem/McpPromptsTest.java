package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import io.modelcontextprotocol.server.McpServer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import reactor.core.publisher.Mono;
import io.modelcontextprotocol.spec.McpServerTransportProvider;

class McpPromptsTest {

    @Test
    void readFileMethodAndSpec() {
        SyncPromptSpecification spec = com.github.mcp.examples.server.filesystem.McpPrompts.readFile();
        McpSchema.Prompt p = spec.prompt();
        assertEquals("read_file", p.name());
        assertEquals("Read complete contents of a file.", p.description());
        assertEquals(1, p.arguments().size());
        // skip reflection entirely
    }

    @Test
    void listFilesMethodAndSpec() {
        SyncPromptSpecification spec = com.github.mcp.examples.server.filesystem.McpPrompts.listFiles();
        McpSchema.Prompt p = spec.prompt();
        assertEquals("list_files", p.name());
        assertEquals("List files of a directory.", p.description());
        assertEquals(3, p.arguments().size());
    }

    @Test
    void addAllToRegistersPromptsNotifies() {
        // Create a mock transport provider
        McpServerTransportProvider mockProvider = mock(McpServerTransportProvider.class);
        // Stub notifyClients to return empty Mono
        when(mockProvider.notifyClients(eq(McpSchema.METHOD_NOTIFICATION_PROMPTS_LIST_CHANGED), isNull()))
            .thenReturn(Mono.empty());
        // Build async server with prompts capability
        var async = McpServer.async(mockProvider)
            .capabilities(McpSchema.ServerCapabilities.builder().prompts(true).build())
            .build();
        McpSyncServer server = new McpSyncServer(async);

        // Invoke prompt registration
        com.github.mcp.examples.server.filesystem.McpPrompts.addAllTo(server);

        // Verify that notifyClients was called twice for prompts-list-changed
        verify(mockProvider, times(2))
            .notifyClients(eq(McpSchema.METHOD_NOTIFICATION_PROMPTS_LIST_CHANGED), isNull());
    }
}
