package com.github.mcp.server.filesystem.declarative;

import com.github.codeboyzhou.mcp.declarative.McpServers;
import com.github.codeboyzhou.mcp.declarative.annotation.McpI18nEnabled;
import com.github.codeboyzhou.mcp.declarative.annotation.McpServerApplication;
import com.github.codeboyzhou.mcp.declarative.server.McpStreamableServerInfo;
import com.github.mcp.server.filesystem.common.ServerInfo;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
@McpI18nEnabled
@McpServerApplication
public class McpStreamableServer {
  /** Main entry for the Streamable HTTP MCP server. */
  public static void main(String[] args) {
    McpStreamableServerInfo serverInfo =
        McpStreamableServerInfo.builder()
            .name(ServerInfo.NAME)
            .version(ServerInfo.VERSION)
            .instructions(ServerInfo.INSTRUCTIONS)
            .requestTimeout(ServerInfo.REQUEST_TIMEOUT)
            .build();
    McpServers.run(McpStreamableServer.class, args).startStreamableServer(serverInfo);
  }
}
