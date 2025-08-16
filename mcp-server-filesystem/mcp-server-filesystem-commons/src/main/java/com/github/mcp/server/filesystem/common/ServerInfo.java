package com.github.mcp.server.filesystem.common;

import java.time.Duration;

public interface ServerInfo {

  String NAME = "mcp-server-filesystem";

  String VERSION = "1.0.0";

  String INSTRUCTIONS = "Filesystem MCP Server";

  Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

  String MESSAGE_ENDPOINT = "/mcp/message";

  String MCP_ENDPOINT = "/mcp";

  String SSE_ENDPOINT = "/sse";
}
