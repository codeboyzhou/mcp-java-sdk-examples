package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;

/**
 * A class that defines resources to the MCP server.
 *
 * @author codeboyzhou
 */
public final class McpResources {

    /**
     * Create a MCP resource that provides access to the file system.
     * Note that this resource is just an example of how to add a resource to the MCP server.
     * So it's not really useful for anything other than demonstration purposes.
     *
     * @return {@link McpServerFeatures.SyncResourceSpecification}
     */
    public static McpServerFeatures.SyncResourceSpecification filesystem() {
        // Step 1: Create a resource with URI, name, description, and MIME type.
        McpSchema.Resource resource = new McpSchema.Resource(
            "file://system", "filesystem", "File system operations interface", "text/plain",
            new McpSchema.Annotations(List.of(McpSchema.Role.ASSISTANT, McpSchema.Role.USER), 1.0)
        );
        // Step 2: Create a handler that returns the contents of the resource.
        return new McpServerFeatures.SyncResourceSpecification(resource, (exchange, request) -> {
            McpSchema.ResourceContents contents = new McpSchema.TextResourceContents(
                resource.uri(), resource.mimeType(), "No specific resource contents, just use the tools."
            );
            return new McpSchema.ReadResourceResult(List.of(contents));
        });
    }

    /**
     * Add all resources to the MCP server.
     *
     * @param server The MCP server to add resources to.
     */
    public static void addAllTo(McpSyncServer server) {
        server.addResource(filesystem());
    }

}
