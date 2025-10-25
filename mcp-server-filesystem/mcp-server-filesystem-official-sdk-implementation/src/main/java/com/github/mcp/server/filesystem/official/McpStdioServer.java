package com.github.mcp.server.filesystem.official;

import com.github.mcp.server.filesystem.common.ServerInfo;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations. This server
 * communicates over standard input/output (STDIO) and provides capabilities for managing filesystem
 * resources through MCP, including finding files, reading file contents, and deleting files.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public class McpStdioServer {
  /** The MCP sync server instance that handles protocol communication over STDIO. */
  private McpSyncServer mcpSyncServer;

  /**
   * Main entry point for the STDIO MCP server. Initializes the server, configures resources,
   * prompts, and tools, and starts listening for MCP messages over standard input/output.
   *
   * @param args command line arguments
   * @throws IOException if an I/O error occurs during server initialization
   */
  public static void main(String[] args) throws IOException {
    // Initialize MCP server
    McpStdioServer mcpStdioServer = new McpStdioServer();
    mcpStdioServer.initialize();
    // Add resources
    mcpStdioServer.mcpSyncServer.addResource(Resources.filesystem());
    // Add prompts
    mcpStdioServer.mcpSyncServer.addPrompt(Prompts.find());
    mcpStdioServer.mcpSyncServer.addPrompt(Prompts.read());
    mcpStdioServer.mcpSyncServer.addPrompt(Prompts.delete());
    // Add tools
    mcpStdioServer.mcpSyncServer.addTool(Tools.find());
    mcpStdioServer.mcpSyncServer.addTool(Tools.read());
    mcpStdioServer.mcpSyncServer.addTool(Tools.delete());
  }

  /**
   * Initialize the STDIO MCP server with the required capabilities and transport provider.
   * Configures the server to support resources, prompts, and tools for filesystem operations and
   * sets up communication over standard input/output.
   */
  private void initialize() {
    McpSchema.ServerCapabilities serverCapabilities =
        McpSchema.ServerCapabilities.builder()
            .resources(true, true)
            .prompts(true)
            .tools(true)
            .build();

    mcpSyncServer =
        McpServer.sync(new StdioServerTransportProvider(McpJsonMapper.getDefault()))
            .serverInfo(ServerInfo.NAME, ServerInfo.VERSION)
            .requestTimeout(ServerInfo.REQUEST_TIMEOUT)
            .instructions(ServerInfo.INSTRUCTIONS)
            .capabilities(serverCapabilities)
            .build();
  }
}
