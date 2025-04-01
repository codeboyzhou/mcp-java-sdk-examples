package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

/**
 * A class that defines prompts for the MCP server.
 *
 * @author codeboyzhou
 */
public final class McpPrompts {

    /**
     * Create a new prompt to the MCP server that to assist to read file.
     * <p>
     * Arguments:
     * filepath (string): The path to the file to read.
     * <p>
     * Returns:
     * result (string): The prompt message like "What is the content of this file: /path/to/file.txt"
     *
     * @return A specification for the MCP prompt.
     */
    public static McpServerFeatures.SyncPromptSpecification readFile() {
        McpSchema.PromptArgument filepath = new McpSchema
            .PromptArgument("filepath", "The path to the file to read", true);

        McpSchema.Prompt prompt = new McpSchema
            .Prompt("read_file", "Read complete contents of a file.", List.of(filepath));

        return new McpServerFeatures.SyncPromptSpecification(
            prompt,
            (exchange, request) -> {
                Map<String, Object> arguments = request.arguments();
                McpSchema.TextContent content = new McpSchema.TextContent(
                    String.format("What is the content of this file: %s", arguments.get("filepath"))
                );
                McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
                return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
            }
        );
    }

    /**
     * Create a new prompt to the MCP server that to assist to list files of a directory non-recursively.
     *
     * @return A specification for the MCP prompt.
     */
    public static McpServerFeatures.SyncPromptSpecification listFiles() {
        McpSchema.PromptArgument directoryPath = new McpSchema
            .PromptArgument("directoryPath", "The path to the directory to read", true);
        McpSchema.PromptArgument fileNamePattern = new McpSchema
            .PromptArgument("fileNamePattern", "Regular expression to filter files", false);

        List<McpSchema.PromptArgument> args = List.of(directoryPath, fileNamePattern);
        McpSchema.Prompt prompt = new McpSchema.Prompt(
            "list_files",
            "List directory files non-recursively with name-based filtering.",
            args
        );

        return new McpServerFeatures.SyncPromptSpecification(
            prompt,
            (exchange, request) -> {
                Map<String, Object> arguments = request.arguments();
                McpSchema.TextContent content = new McpSchema.TextContent(
                    String.format(
                        "What is the list of files in this directory (non-recursive): %s, with file name pattern: %s",
                        arguments.get("directoryPath"),
                        arguments.get("fileNamePattern")
                    )
                );
                McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
                return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
            }
        );
    }

    /**
     * Add all prompts to the MCP server.
     *
     * @param server The MCP server to add prompts to.
     */
    public static void addAllTo(McpSyncServer server) {
        server.addPrompt(readFile());
        server.addPrompt(listFiles());
    }

}
