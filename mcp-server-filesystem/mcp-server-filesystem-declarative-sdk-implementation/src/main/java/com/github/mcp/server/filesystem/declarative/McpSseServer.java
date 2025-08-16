package com.github.mcp.server.filesystem.declarative;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.annotation.McpI18nEnabled;
import com.github.codeboyzhou.mcp.declarative.annotation.McpServerApplication;
import com.github.codeboyzhou.mcp.declarative.server.factory.McpSseServerInfo;
import com.github.mcp.server.filesystem.common.ServerInfo;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
@McpI18nEnabled
@McpServerApplication
public class McpSseServer {

  /** Main entry for the HTTP SSE MCP server. */
  public static void main(String[] args) {
    McpSseServerInfo serverInfo =
        McpSseServerInfo.builder()
            .name(ServerInfo.NAME)
            .version(ServerInfo.VERSION)
            .instructions(ServerInfo.INSTRUCTIONS)
            .requestTimeout(ServerInfo.REQUEST_TIMEOUT)
            .build();
    McpServers.run(McpSseServer.class, args).startSseServer(serverInfo);
  }
}
