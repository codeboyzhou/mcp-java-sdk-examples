package com.github.mcp.server.filesystem.official;

import static com.github.mcp.server.filesystem.common.ServerInfo.MESSAGE_ENDPOINT;
import static com.github.mcp.server.filesystem.common.ServerInfo.SSE_ENDPOINT;

import com.github.mcp.server.filesystem.common.HttpServer;
import com.github.mcp.server.filesystem.common.ServerInfo;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpSseServer {

  private static final Logger log = LoggerFactory.getLogger(McpSseServer.class);

  /** The HTTP SSE transport provider. */
  private static HttpServletSseServerTransportProvider transport;

  /** The MCP sync server instance. */
  private McpSyncServer server;

  /** Main entry for the HTTP SSE MCP server. */
  public static void main(String[] args) throws IOException {
    // Initialize MCP server
    McpSseServer mcpSseServer = new McpSseServer();
    mcpSseServer.initialize();
    // Add resources
    mcpSseServer.server.addResource(Resources.filesystem());
    // Add prompts
    mcpSseServer.server.addPrompt(Prompts.find());
    mcpSseServer.server.addPrompt(Prompts.read());
    mcpSseServer.server.addPrompt(Prompts.delete());
    // Add tools
    mcpSseServer.server.addTool(Tools.find());
    mcpSseServer.server.addTool(Tools.read());
    mcpSseServer.server.addTool(Tools.delete());
    // Start HTTP server
    HttpServer httpserver = new HttpServer();
    httpserver.use(transport).bind(8080).start();
  }

  /** Initialize the HTTP SSE MCP server. */
  private void initialize() {
    McpSchema.ServerCapabilities serverCapabilities =
        McpSchema.ServerCapabilities.builder()
            .resources(true, true)
            .prompts(true)
            .tools(true)
            .build();

    transport =
        HttpServletSseServerTransportProvider.builder()
            .sseEndpoint(SSE_ENDPOINT)
            .messageEndpoint(MESSAGE_ENDPOINT)
            .build();

    server =
        McpServer.sync(transport)
            .serverInfo(ServerInfo.NAME, ServerInfo.VERSION)
            .capabilities(serverCapabilities)
            .build();
  }
}
