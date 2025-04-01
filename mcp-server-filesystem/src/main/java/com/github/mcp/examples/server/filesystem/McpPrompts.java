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
    public static McpServerFeatures.SyncPromptSpecification fileReadingPrompt() {
        McpSchema.PromptArgument argument = new McpSchema
            .PromptArgument("filepath", "The path to the file to read", true);

        McpSchema.Prompt prompt = new McpSchema
            .Prompt("read_file", "Read complete contents of a file.", List.of(argument));

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
     * Create a new prompt to the MCP server that to assist to read directory.
     *
     * @return A specification for the MCP prompt.
     */
    public static McpServerFeatures.SyncPromptSpecification dirReadingPrompt() {
        McpSchema.PromptArgument dirPath = new McpSchema
            .PromptArgument("dirPath", "The path to the dir to read", true);
        McpSchema.PromptArgument fileNamePattern = new McpSchema
            .PromptArgument("fileNamePattern", "Regular expression to filter files", false);

        McpSchema.Prompt prompt = new McpSchema.Prompt(
            "read_dir",
            "Enumerate directory files with name-based filtering.",
            List.of(dirPath, fileNamePattern)
        );

        return new McpServerFeatures.SyncPromptSpecification(
            prompt,
            (exchange, request) -> {
                Map<String, Object> arguments = request.arguments();
                McpSchema.TextContent content = new McpSchema.TextContent(
                    String.format(
                        "What is the list of files in this directory: %s, with file name pattern: %s",
                        arguments.get("dirPath"),
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
        server.addPrompt(fileReadingPrompt());
        server.addPrompt(dirReadingPrompt());
    }

}
