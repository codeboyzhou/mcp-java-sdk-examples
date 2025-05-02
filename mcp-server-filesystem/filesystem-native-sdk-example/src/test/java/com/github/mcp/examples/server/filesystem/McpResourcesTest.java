package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import reactor.core.publisher.Mono;
import io.modelcontextprotocol.spec.McpServerTransportProvider;

@com.github.codeboyzhou.mcp.declarative.annotation.McpResources
class McpResourcesTest {

    @Test
    void filesystemSpecHasCorrectResource() {
        // call the method and inspect the spec
        SyncResourceSpecification spec = com.github.mcp.examples.server.filesystem.McpResources.filesystem();
        McpSchema.Resource resource = spec.resource();
        assertEquals("file://system", resource.uri());
        assertEquals("filesystem", resource.name());
        assertEquals("File system operations interface", resource.description());
    }

    @Test
    void addAllToRegistersResourceNotifies() {
        // Create a mock transport provider
        McpServerTransportProvider mockProvider = mock(McpServerTransportProvider.class);
        // Stub notifyClients to return empty Mono
        when(mockProvider.notifyClients(eq(McpSchema.METHOD_NOTIFICATION_RESOURCES_LIST_CHANGED), isNull()))
            .thenReturn(Mono.empty());
        // Build async server with resources capability
        var async = McpServer.async(mockProvider)
            .capabilities(McpSchema.ServerCapabilities.builder().resources(true, true).build())
            .build();
        McpSyncServer server = new McpSyncServer(async);

        // Invoke resource registration
        McpResources.addAllTo(server);

        // Verify that notifyClients was called once for resources-list-changed
        verify(mockProvider, times(1))
            .notifyClients(eq(McpSchema.METHOD_NOTIFICATION_RESOURCES_LIST_CHANGED), isNull());
    }
}
