package com.github.mcp.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.annotation.McpI18nEnabled;
import com.github.codeboyzhou.mcp.declarative.annotation.McpServerApplication;

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
    McpServers.run(McpSseServer.class, args).startServer();
  }
}
