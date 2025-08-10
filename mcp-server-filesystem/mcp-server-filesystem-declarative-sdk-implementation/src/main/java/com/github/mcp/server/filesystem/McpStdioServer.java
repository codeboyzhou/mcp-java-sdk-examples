package com.github.mcp.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.annotation.McpI18nEnabled;
import com.github.codeboyzhou.mcp.declarative.annotation.McpServerApplication;
import com.github.codeboyzhou.mcp.declarative.server.McpServerInfo;
import com.github.mcp.server.filesystem.common.ServerInfo;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
@McpI18nEnabled
@McpServerApplication
public class McpStdioServer {

  /** Main entry for the STDIO MCP server. */
  public static void main(String[] args) {
    McpServerInfo serverInfo =
        McpServerInfo.builder().name(ServerInfo.NAME).version(ServerInfo.VERSION).build();
    McpServers.run(McpStdioServer.class, args).startStdioServer(serverInfo);
  }
}
