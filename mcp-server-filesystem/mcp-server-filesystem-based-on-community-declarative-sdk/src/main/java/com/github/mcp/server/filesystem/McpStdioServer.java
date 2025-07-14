package com.github.mcp.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.server.McpServerInfo;
import com.github.mcp.server.filesystem.util.ServerInfo;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpStdioServer {

    public static void main(String[] args) {
        McpServerInfo serverInfo = McpServerInfo.builder()
            .name(ServerInfo.SERVER_NAME)
            .version(ServerInfo.SERVER_VERSION)
            .build();
        McpServers.run(McpStdioServer.class, args).startStdioServer(serverInfo);
    }

}
