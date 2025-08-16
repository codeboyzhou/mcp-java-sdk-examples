package com.github.mcp.server.filesystem.official;

import com.github.mcp.server.filesystem.common.HttpServer;
import com.github.mcp.server.filesystem.common.ServerInfo;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpStreamableServer {

  private static final Logger log = LoggerFactory.getLogger(McpStreamableServer.class);

  /** The Streamable HTTP transport provider. */
  private static HttpServletStreamableServerTransportProvider transport;

  /** The MCP sync server instance. */
  private McpSyncServer server;

  /** Main entry for the Streamable HTTP MCP server. */
  public static void main(String[] args) throws IOException {
    // Initialize MCP server
    McpStreamableServer mcpStreamableServer = new McpStreamableServer();
    mcpStreamableServer.initialize();
    // Add resources
    mcpStreamableServer.server.addResource(Resources.filesystem());
    // Add prompts
    mcpStreamableServer.server.addPrompt(Prompts.find());
    mcpStreamableServer.server.addPrompt(Prompts.read());
    mcpStreamableServer.server.addPrompt(Prompts.delete());
    // Add tools
    mcpStreamableServer.server.addTool(Tools.find());
    mcpStreamableServer.server.addTool(Tools.read());
    mcpStreamableServer.server.addTool(Tools.delete());
    // Start HTTP server
    HttpServer httpserver = new HttpServer();
    httpserver.use(transport).bind(8080).start();
  }

  /** Initialize the Streamable HTTP MCP server. */
  private void initialize() {
    McpSchema.ServerCapabilities serverCapabilities =
        McpSchema.ServerCapabilities.builder()
            .resources(true, true)
            .prompts(true)
            .tools(true)
            .build();

    transport =
        HttpServletStreamableServerTransportProvider.builder()
            .mcpEndpoint(ServerInfo.MCP_ENDPOINT)
            .build();

    server =
        McpServer.sync(transport)
            .serverInfo(ServerInfo.NAME, ServerInfo.VERSION)
            .capabilities(serverCapabilities)
            .build();
  }
}
