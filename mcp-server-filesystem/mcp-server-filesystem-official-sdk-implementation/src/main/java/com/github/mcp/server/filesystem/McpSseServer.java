package com.github.mcp.server.filesystem;

import static com.github.mcp.server.filesystem.common.ServerInfo.MESSAGE_ENDPOINT;
import static com.github.mcp.server.filesystem.common.ServerInfo.SSE_ENDPOINT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mcp.server.filesystem.common.ServerInfo;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
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
  private HttpServletSseServerTransportProvider transport;

  /** The MCP sync server instance. */
  private McpSyncServer server;

  /** Main entry for the HTTP SSE MCP server. */
  public static void main(String[] args) throws IOException {
    // Initialize MCP server
    McpSseServer mcpSseServer = new McpSseServer();
    mcpSseServer.initialize();
    // Add resources
    mcpSseServer.server.addResource(McpResources.filesystem());
    // Add prompts
    mcpSseServer.server.addPrompt(McpPrompts.find());
    mcpSseServer.server.addPrompt(McpPrompts.read());
    mcpSseServer.server.addPrompt(McpPrompts.delete());
    // Add tools
    mcpSseServer.server.addTool(McpTools.find());
    mcpSseServer.server.addTool(McpTools.read());
    mcpSseServer.server.addTool(McpTools.delete());
    // Start HTTP server
    mcpSseServer.startHttpServer(mcpSseServer.transport);
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
        new HttpServletSseServerTransportProvider(
            new ObjectMapper(), MESSAGE_ENDPOINT, SSE_ENDPOINT);
    server =
        McpServer.sync(transport)
            .serverInfo(ServerInfo.NAME, ServerInfo.VERSION)
            .capabilities(serverCapabilities)
            .build();
  }

  /** Start the HTTP server with Jetty. */
  private void startHttpServer(HttpServletSseServerTransportProvider transport) {
    ServletContextHandler servletContextHandler =
        new ServletContextHandler(ServletContextHandler.SESSIONS);
    servletContextHandler.setContextPath("/");

    ServletHolder servletHolder = new ServletHolder(transport);
    servletContextHandler.addServlet(servletHolder, "/*");

    Server httpserver = new Server(8080);
    httpserver.setHandler(servletContextHandler);

    try {
      httpserver.start();
      log.info("Jetty-based HTTP server started on http://127.0.0.1:8080");

      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(
                  () -> {
                    try {
                      log.info("Shutting down HTTP server");
                      httpserver.stop();
                      server.close();
                    } catch (Exception e) {
                      log.error("Error stopping HTTP server", e);
                    }
                  }));

      // Wait for the HTTP server to stop
      httpserver.join();
    } catch (Exception e) {
      log.error("Error starting HTTP server on http://127.0.0.1:8080", e);
      server.close();
    }
  }
}
