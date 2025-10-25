package com.github.mcp.server.filesystem.common;

import java.time.Duration;

/**
 * Interface that defines common server information constants. This interface holds various
 * configuration values and endpoints used by the MCP filesystem server.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public interface ServerInfo {
  /** The name of the server application. */
  String NAME = "mcp-server-filesystem";

  /** The version of the server application. */
  String VERSION = "1.0.0";

  /** Instructions or description of the server application. */
  String INSTRUCTIONS = "Filesystem MCP Server";

  /** The timeout duration for server requests. */
  Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

  /** The endpoint path for handling MCP messages. */
  String MESSAGE_ENDPOINT = "/mcp/message";

  /** The base endpoint path for MCP services. */
  String MCP_ENDPOINT = "/mcp";

  /** The endpoint path for Server-Sent Events (SSE). */
  String SSE_ENDPOINT = "/sse";
}
