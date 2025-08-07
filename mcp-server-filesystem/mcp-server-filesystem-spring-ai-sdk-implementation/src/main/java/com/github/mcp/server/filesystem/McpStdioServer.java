package com.github.mcp.server.filesystem;

import java.util.List;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * There is currently no MCP resource and prompt for this server because the Spring AI MCP Server
 * has not support @Resource and @Prompt yet.
 *
 * @author codeboyzhou
 */
@SpringBootApplication
public class McpStdioServer {

  /** Main entry for the STDIO MCP server. */
  public static void main(String[] args) {
    SpringApplication.run(McpStdioServer.class, args);
  }

  @Bean
  public List<ToolCallback> toolCallbacks(McpTools mcpTools) {
    return List.of(ToolCallbacks.from(mcpTools));
  }
}
