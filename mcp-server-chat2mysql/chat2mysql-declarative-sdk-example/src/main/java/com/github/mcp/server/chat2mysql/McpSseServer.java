package com.github.mcp.server.chat2mysql;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.server.McpSseServerInfo;
import com.github.mcp.server.chat2mysql.util.ServerInfo;

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
        McpSseServerInfo serverInfo = McpSseServerInfo.builder()
            .name(ServerInfo.SERVER_NAME)
            .version(ServerInfo.SERVER_VERSION)
            .build();
        McpServers.run(McpSseServer.class, args).startSyncSseServer(serverInfo);
    }

}
