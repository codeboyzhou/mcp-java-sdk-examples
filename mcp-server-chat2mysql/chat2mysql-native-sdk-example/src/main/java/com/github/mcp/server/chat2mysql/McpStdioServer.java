package com.github.mcp.server.chat2mysql;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import static com.github.mcp.server.chat2mysql.util.ServerInfo.SERVER_NAME;
import static com.github.mcp.server.chat2mysql.util.ServerInfo.SERVER_VERSION;

/**
 * Java server implementing Model Context Protocol (MCP) for chat2mysql.
 * Use AI agent that supports multi-language to chat with your MySQL database.
 *
 * @author codeboyzhou
 */
public class McpStdioServer {

    /**
     * The MCP sync server instance.
     */
    private McpSyncServer server;

    /**
     * Initialize the STDIO MCP server.
     */
    private void initialize() {
        McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
            .tools(true)
            .prompts(true)
            .resources(true, true)
            .build();

        server = McpServer.sync(new StdioServerTransportProvider())
            .serverInfo(SERVER_NAME, SERVER_VERSION)
            .capabilities(serverCapabilities)
            .build();

        // We are in STDIO mode, so logging is unavailable and messages are output to STDERR only
        System.err.println(SERVER_NAME + " " + SERVER_VERSION + " initialized in STDIO mode");
    }

    /**
     * Main entry point for the STDIO MCP server.
     */
    public static void main(String[] args) {
        // Initialize MCP server
        McpStdioServer mcpStdioServer = new McpStdioServer();
        mcpStdioServer.initialize();
        // Add resources, prompts, and tools to the MCP server
        // McpResources.addAllTo(mcpStdioServer.server);
        McpPrompts.addAllTo(mcpStdioServer.server);
        // McpTools.addAllTo(mcpStdioServer.server);
    }

}
