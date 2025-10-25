package com.github.mcp.server.filesystem.spring;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * There is currently no MCP resource and prompt for this server because the Spring AI MCP Server
 * has not support {@code @Resource} and {@code @Prompt} yet.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
@SpringBootApplication
public class McpStdioServer {
  /** Main entry for the STDIO MCP server. */
  public static void main(String[] args) {
    SpringApplication.run(McpStdioServer.class, args);
  }

  @Bean
  public ToolCallbackProvider filesystemTools(Tools tools) {
    return MethodToolCallbackProvider.builder().toolObjects(tools).build();
  }
}
