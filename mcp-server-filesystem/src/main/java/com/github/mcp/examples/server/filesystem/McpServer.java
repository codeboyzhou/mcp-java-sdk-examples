package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpServer {
    /**
     * The name of the MCP server.
     */
    private static final String SERVER_NAME = "mcp-server-filesystem";

    /**
     * The version of the MCP server.
     */
    private static final String SERVER_VERSION = "0.1.0";

    /**
     * The MCP sync server instance.
     */
    private McpSyncServer server;

    /**
     * Initializes the MCP server on STDIO.
     */
    private void initialize() {
        McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
            .tools(true)
            .prompts(true)
            .resources(false, true)
            .build();

        server = io.modelcontextprotocol.server.McpServer.sync(new StdioServerTransportProvider())
            .serverInfo(SERVER_NAME, SERVER_VERSION)
            .capabilities(serverCapabilities)
            .build();

        // We are in STDIO mode, so logging is unavailable and messages are output to STDERR only
        System.err.println(SERVER_NAME + " " + SERVER_VERSION + " initialized in STDIO mode");
    }

    /**
     * Main entry point for the MCP server on STDIO.
     */
    public static void main(String[] args) {
        // Initialize MCP server
        McpServer mcpServer = new McpServer();
        mcpServer.initialize();
        // Add resources, prompts and tools to the MCP server
        McpResources.addAllTo(mcpServer.server);
        McpPrompts.addAllTo(mcpServer.server);
        McpTools.addAllTo(mcpServer.server);
    }

}
