package com.github.mcp.server.chat2mysql;

import com.github.codeboyzhou.mcp.declarative.McpServers;

/**
 * Java server implementing Model Context Protocol (MCP) for chat2mysql.
 * Use AI agent that supports multi-language to chat with your MySQL database.
 *
 * @author codeboyzhou
 */
public class McpSseServer {

    /**
     * Main entry point for the HTTP SSE MCP server.
     */
    public static void main(String[] args) {
        McpServers.run(McpSseServer.class, args).startServer();
    }

}
