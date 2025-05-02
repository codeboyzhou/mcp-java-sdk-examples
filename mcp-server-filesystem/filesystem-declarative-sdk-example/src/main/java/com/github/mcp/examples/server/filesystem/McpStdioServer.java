package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.server.McpServerInfo;

import static com.github.mcp.examples.server.filesystem.util.ServerInfo.SERVER_NAME;
import static com.github.mcp.examples.server.filesystem.util.ServerInfo.SERVER_VERSION;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpStdioServer {

    public static void main(String[] args) {
        McpServerInfo serverInfo = McpServerInfo.builder().name(SERVER_NAME).version(SERVER_VERSION).build();
        McpServers.run(McpStdioServer.class, args).startSyncStdioServer(serverInfo);
    }

}
