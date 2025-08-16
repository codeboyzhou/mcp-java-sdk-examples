package com.github.mcp.server.filesystem.official;

import com.github.mcp.server.filesystem.common.ServerInfo;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpStdioServer {

  private static final Logger log = LoggerFactory.getLogger(McpStdioServer.class);

  /** The MCP sync server instance. */
  private McpSyncServer server;

  /** Main entry for the STDIO MCP server. */
  public static void main(String[] args) throws IOException {
    // Initialize MCP server
    McpStdioServer mcpStdioServer = new McpStdioServer();
    mcpStdioServer.initialize();
    // Add resources
    mcpStdioServer.server.addResource(Resources.filesystem());
    // Add prompts
    mcpStdioServer.server.addPrompt(Prompts.find());
    mcpStdioServer.server.addPrompt(Prompts.read());
    mcpStdioServer.server.addPrompt(Prompts.delete());
    // Add tools
    mcpStdioServer.server.addTool(Tools.find());
    mcpStdioServer.server.addTool(Tools.read());
    mcpStdioServer.server.addTool(Tools.delete());
  }

  /** Initialize the STDIO MCP server. */
  private void initialize() {
    McpSchema.ServerCapabilities serverCapabilities =
        McpSchema.ServerCapabilities.builder()
            .resources(true, true)
            .prompts(true)
            .tools(true)
            .build();

    server =
        McpServer.sync(new StdioServerTransportProvider())
            .serverInfo(ServerInfo.NAME, ServerInfo.VERSION)
            .requestTimeout(ServerInfo.REQUEST_TIMEOUT)
            .instructions(ServerInfo.INSTRUCTIONS)
            .capabilities(serverCapabilities)
            .build();

    log.info("{} {} running on stdio%n", ServerInfo.NAME, ServerInfo.VERSION);
  }
}
