package com.github.mcp.examples;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class McpSyncStdioServer {

    private McpSyncServer mcpSyncServer;

    public static void main(String[] args) {
        McpSyncStdioServer server = new McpSyncStdioServer();
        server.initialize();
        server.addResource();
        server.addPrompt();
        server.addTool();
    }

    public void initialize() {
        McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
            .logging()
            .tools(true)
            .prompts(true)
            .resources(false, true)
            .build();

        mcpSyncServer = McpServer.sync(new StdioServerTransportProvider())
            .serverInfo("MCP Stdio Server: Built with native MCP Java SDK (No Spring)", "1.0.0")
            .capabilities(serverCapabilities)
            .build();

        mcpSyncServer.loggingNotification(McpSchema.LoggingMessageNotification.builder()
            .level(McpSchema.LoggingLevel.INFO)
            .logger("MCP Stdio Server")
            .data("MCP Stdio Server Started")
            .build()
        );
    }

    public void addResource() {
        McpSchema.Resource resource = new McpSchema.Resource(
            "system://variables",
            "Environment Variables",
            "Provides access to all environment variables from the MCP server's underlying host system.",
            "text/plain",
            new McpSchema.Annotations(List.of(McpSchema.Role.ASSISTANT, McpSchema.Role.USER), 1.0)
        );

        McpServerFeatures.SyncResourceSpecification resourceSpec = new McpServerFeatures.SyncResourceSpecification(resource,
            (exchange, request) -> {
                McpSchema.ResourceContents contents = new McpSchema.TextResourceContents(
                    resource.uri(), resource.mimeType(), System.getenv().toString()
                );
                return new McpSchema.ReadResourceResult(List.of(contents));
            }
        );

        mcpSyncServer.addResource(resourceSpec);
    }

    public void addPrompt() {
        McpSchema.PromptArgument argument = new McpSchema.PromptArgument(
            "runtime",
            "Specify a runtime keyword, LLMs will locate its complete installation dir.",
            true
        );
        McpSchema.Prompt prompt = new McpSchema.Prompt(
            "Runtime Installation Dir Finder",
            "Use LLMs to identify the exact installation location for the target runtime.",
            List.of(argument)
        );

        McpServerFeatures.SyncPromptSpecification promptSpec = new McpServerFeatures.SyncPromptSpecification(prompt,
            (exchange, request) -> {
                Map<String, Object> arguments = request.arguments();
                McpSchema.TextContent content = new McpSchema.TextContent(
                    String.format("Where is %s installed on this server", arguments.get("runtime"))
                );
                McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
                return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
            }
        );

        mcpSyncServer.addPrompt(promptSpec);
    }

    public void addTool() {
        final String argumentsJsonSchema = """
                {
                    "$schema": "http://json-schema.org/draft-07/schema#",
                    "type": "object",
                    "properties": {
                        "runtime": {
                            "type": "string",
                            "description": "You can tell LLMs a runtime and attempt to locate its installation dir",
                            "minLength": 1
                        }
                    },
                    "required": [
                        "runtime"
                    ]
                }
            """;

        McpSchema.Tool tool = new McpSchema.Tool(
            "Runtime Installation Dir Finder",
            "Identify the installation dir for the specified runtime via LLMs.",
            argumentsJsonSchema
        );

        McpServerFeatures.SyncToolSpecification toolSpec = new McpServerFeatures.SyncToolSpecification(tool,
            (exchange, arguments) -> {
                final String runtime = arguments.get("runtime").toString();
                String[] pathVariables = System.getenv("PATH").split(";");
                final String result = Arrays.stream(pathVariables)
                    .filter(v -> v.toLowerCase().contains(runtime))
                    .findFirst()
                    .orElse(String.format("Not found %s on this server", runtime));
                McpSchema.Content content = new McpSchema.TextContent(result);
                return new McpSchema.CallToolResult(List.of(content), false);
            }
        );

        mcpSyncServer.addTool(toolSpec);
    }

}
