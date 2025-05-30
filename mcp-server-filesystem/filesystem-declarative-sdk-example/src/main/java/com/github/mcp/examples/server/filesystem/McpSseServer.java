package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.server.McpSseServerInfo;
import com.github.mcp.examples.server.filesystem.util.ServerInfo;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
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
