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
     * Create a new resource to the MCP server that provides access to the file system.
     * Note that this resource is just an example of how to add a resource to the MCP server.
     * So it's not really useful for anything other than demonstration purposes.
     *
     * @return A specification for the MCP resource.
     */
    public static McpServerFeatures.SyncResourceSpecification fileSystemResource() {
        McpSchema.Resource resource = new McpSchema.Resource(
            "file://system",
            "filesystem",
            "File system operations interface",
            "text/plain",
            new McpSchema.Annotations(List.of(McpSchema.Role.ASSISTANT, McpSchema.Role.USER), 1.0)
        );

        return new McpServerFeatures.SyncResourceSpecification(
            resource,
            (exchange, request) -> {
                McpSchema.ResourceContents contents = new McpSchema.TextResourceContents(
                    resource.uri(), resource.mimeType(), "No specific resource contents, just use the tools."
                );
                return new McpSchema.ReadResourceResult(List.of(contents));
            }
        );
    }

    /**
     * Add all resources to the MCP server.
     *
     * @param server The MCP server to add resources to.
     */
    public static void addAllTo(McpSyncServer server) {
        server.addResource(fileSystemResource());
    }

}
