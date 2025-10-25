package com.github.mcp.server.filesystem.official;

import com.github.mcp.server.filesystem.common.HttpServer;
import com.github.mcp.server.filesystem.common.ServerInfo;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations. This server
 * communicates over streamable HTTP server and provides capabilities for managing filesystem
 * resources through MCP, including finding files, reading file contents, and deleting files.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public class McpStreamableServer {
  /** The Streamable HTTP transport provider for handling HTTP communication. */
  private static HttpServletStreamableServerTransportProvider transport;

  /** The MCP sync server instance that handles protocol communication. */
  private McpSyncServer mcpSyncServer;

  /**
   * Main entry point for the Streamable HTTP MCP server. Initializes the server, configures
   * resources, prompts, and tools, and starts the HTTP server on port 8080 with streamable
   * transport.
   *
   * @param args command line arguments
   * @throws IOException if an I/O error occurs during server initialization or startup
   */
  public static void main(String[] args) throws IOException {
    // Initialize MCP server
    McpStreamableServer mcpStreamableServer = new McpStreamableServer();
    mcpStreamableServer.initialize();
    // Add resources
    mcpStreamableServer.mcpSyncServer.addResource(Resources.filesystem());
    // Add prompts
    mcpStreamableServer.mcpSyncServer.addPrompt(Prompts.find());
    mcpStreamableServer.mcpSyncServer.addPrompt(Prompts.read());
    mcpStreamableServer.mcpSyncServer.addPrompt(Prompts.delete());
    // Add tools
    mcpStreamableServer.mcpSyncServer.addTool(Tools.find());
    mcpStreamableServer.mcpSyncServer.addTool(Tools.read());
    mcpStreamableServer.mcpSyncServer.addTool(Tools.delete());
    // Start HTTP server
    HttpServer httpserver = new HttpServer();
    httpserver.use(transport).bind(8080).start();
  }

  /**
   * Initialize the Streamable HTTP MCP server with the required capabilities and transport
   * provider. Configures the server to support resources, prompts, and tools for filesystem
   * operations and sets up the streamable HTTP transport mechanism.
   */
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

    mcpSyncServer =
        McpServer.sync(transport)
            .serverInfo(ServerInfo.NAME, ServerInfo.VERSION)
            .requestTimeout(ServerInfo.REQUEST_TIMEOUT)
            .instructions(ServerInfo.INSTRUCTIONS)
            .capabilities(serverCapabilities)
            .build();
  }
}
