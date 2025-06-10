package com.github.mcp.server.chat2mysql;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.annotation.McpComponentScan;
import com.github.codeboyzhou.mcp.declarative.server.McpServerInfo;
import com.github.mcp.server.chat2mysql.util.ServerInfo;

/**
 * Java server implementing Model Context Protocol (MCP) for chat2mysql.
 * Use AI agent that supports multi-language to chat with your MySQL database.
 *
 * @author codeboyzhou
 */
@McpComponentScan(basePackageClass = McpStdioServer.class)
public class McpStdioServer {

    /**
     * Main entry point for the HTTP SSE MCP server.
     */
    public static void main(String[] args) {
        McpServerInfo serverInfo = McpServerInfo.builder()
            .name(ServerInfo.SERVER_NAME)
            .version(ServerInfo.SERVER_VERSION)
            .build();
        McpServers.run(McpStdioServer.class, args).startStdioServer(serverInfo);
    }

}
