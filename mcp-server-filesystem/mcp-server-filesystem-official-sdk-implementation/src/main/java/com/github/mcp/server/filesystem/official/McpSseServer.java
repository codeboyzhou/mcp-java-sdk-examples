package com.github.mcp.server.filesystem.official;

import com.github.mcp.server.filesystem.common.HttpServer;
import com.github.mcp.server.filesystem.common.ServerInfo;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations. This server
 * communicates over HTTP SSE (Server-Sent Events) and provides capabilities for managing filesystem
 * resources through MCP, including finding files, reading file contents, and deleting files.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public class McpSseServer {
  /** The HTTP SSE transport provider for handling server-sent events. */
  private static HttpServletSseServerTransportProvider transport;

  /** The MCP sync server instance that handles protocol communication. */
  private McpSyncServer mcpSyncServer;

  /**
   * Main entry point for the HTTP SSE MCP server. Initializes the server, configures resources,
   * prompts, and tools, and starts the HTTP server on port 8080.
   *
   * @param args command line arguments
   * @throws IOException if an I/O error occurs during server initialization or startup
   */
  public static void main(String[] args) throws IOException {
    // Initialize MCP server
    McpSseServer mcpSseServer = new McpSseServer();
    mcpSseServer.initialize();
    // Add resources
    mcpSseServer.mcpSyncServer.addResource(Resources.filesystem());
    // Add prompts
    mcpSseServer.mcpSyncServer.addPrompt(Prompts.find());
    mcpSseServer.mcpSyncServer.addPrompt(Prompts.read());
    mcpSseServer.mcpSyncServer.addPrompt(Prompts.delete());
    // Add tools
    mcpSseServer.mcpSyncServer.addTool(Tools.find());
    mcpSseServer.mcpSyncServer.addTool(Tools.read());
    mcpSseServer.mcpSyncServer.addTool(Tools.delete());
    // Start HTTP server
    HttpServer httpserver = new HttpServer();
    httpserver.use(transport).bind(8080).start();
  }

  /**
   * Initialize the HTTP SSE MCP server with the required capabilities and transport provider.
   * Configures the server to support resources, prompts, and tools for filesystem operations.
   */
  private void initialize() {
    McpSchema.ServerCapabilities serverCapabilities =
        McpSchema.ServerCapabilities.builder()
            .resources(true, true)
            .prompts(true)
            .tools(true)
            .build();

    transport =
        HttpServletSseServerTransportProvider.builder()
            .sseEndpoint(ServerInfo.SSE_ENDPOINT)
            .messageEndpoint(ServerInfo.MESSAGE_ENDPOINT)
            .build();

    mcpSyncServer =
        McpServer.sync(transport)
            .serverInfo(ServerInfo.NAME, ServerInfo.VERSION)
            .requestTimeout(ServerInfo.REQUEST_TIMEOUT)
            .instructions(ServerInfo.INSTRUCTIONS)
            .capabilities(serverCapabilities)
            .build();
  }
}
