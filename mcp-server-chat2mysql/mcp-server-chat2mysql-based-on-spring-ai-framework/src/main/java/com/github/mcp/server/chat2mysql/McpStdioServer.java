package com.github.mcp.server.chat2mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * There is currently no MCP prompt for this server because the Spring AI MCP Server has not support @Prompt yet.
 */
@SpringBootApplication
public class McpStdioServer {

    public static void main(String[] args) {
        SpringApplication.run(McpStdioServer.class, args);
    }

}
