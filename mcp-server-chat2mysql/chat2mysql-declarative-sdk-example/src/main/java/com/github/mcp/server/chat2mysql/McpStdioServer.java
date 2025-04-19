package com.github.mcp.server.chat2mysql;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.annotation.McpComponentScan;
import com.github.codeboyzhou.mcp.declarative.server.McpServerInfo;

import static com.github.mcp.server.chat2mysql.util.ServerInfo.SERVER_NAME;
import static com.github.mcp.server.chat2mysql.util.ServerInfo.SERVER_VERSION;

@McpComponentScan(basePackageClass = McpStdioServer.class)
public class McpStdioServer {

    public static void main(String[] args) {
        McpServers servers = McpServers.run(McpStdioServer.class, args);
        servers.startSyncStdioServer(McpServerInfo.builder().name(SERVER_NAME).version(SERVER_VERSION).build());
    }

}
