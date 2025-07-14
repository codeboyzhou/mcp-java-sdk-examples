package com.github.mcp.server.filesystem;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * There is currently no MCP resource and prompt for this server
 * because the Spring AI MCP Server has not support @Resource and @Prompt yet.
 */
@SpringBootApplication
public class McpStdioServer {

    public static void main(String[] args) {
        SpringApplication.run(McpStdioServer.class, args);
    }

    @Bean
    public List<ToolCallback> myMcpTools(MyMcpTools myMcpTools) {
        return List.of(ToolCallbacks.from(myMcpTools));
    }

}
