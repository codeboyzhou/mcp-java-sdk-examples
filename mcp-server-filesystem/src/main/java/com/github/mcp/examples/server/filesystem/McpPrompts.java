package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.util.Utils;

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
     * Prompt Arguments:
     * <p>
     * filepath (string): The path to the file to read.
     * <p>
     * Prompt Messages:
     * <p>
     * result (string): The prompt message like
     * <ul>
     *     <li>What is the content of this file: /path/to/file.txt</li>
     * </ul>
     *
     * @return A specification for the MCP prompt.
     */
    public static McpServerFeatures.SyncPromptSpecification readFile() {
        McpSchema.PromptArgument filepath = new McpSchema
            .PromptArgument("filepath", "The path to the file to read", true);

        McpSchema.Prompt prompt = new McpSchema
            .Prompt("read_file", "Read complete file contents with UTF-8 encoding.", List.of(filepath));

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
     * Create a new prompt to the MCP server that to assist to list files of a directory.
     * <p>
     * Prompt Arguments:
     * <p>
     * directoryPath (string): The path to the directory to read.
     * <p>
     * fileNamePattern (string): Regular expression to filter files.
     * <p>
     * recursive (boolean): Whether to list files recursively.
     * <p>
     * Prompt Messages:
     * <p>
     * result (string): The prompt message like
     * <ul>
     *     <li>Please list files in this directory: /path/to</li>
     *     <li>Please list files in this directory: /path/to, with file name pattern: \.txt</li>
     *     <li>Please list files in this directory: /path/to, recursively, with file name pattern: \.txt</li>
     * </ul>
     *
     * @return A specification for the MCP prompt.
     */
    public static McpServerFeatures.SyncPromptSpecification listFiles() {
        McpSchema.PromptArgument directoryPath = new McpSchema
            .PromptArgument("directoryPath", "The path to the directory to read", true);
        McpSchema.PromptArgument fileNamePattern = new McpSchema
            .PromptArgument("fileNamePattern", "Regular expression to filter files", false);
        McpSchema.PromptArgument recursive = new McpSchema
            .PromptArgument("recursive", "Whether to list files recursively", false);

        List<McpSchema.PromptArgument> args = List.of(directoryPath, fileNamePattern, recursive);
        McpSchema.Prompt prompt = new McpSchema
            .Prompt("list_files", "List directory files with name-based filtering.", args);

        return new McpServerFeatures.SyncPromptSpecification(
            prompt,
            (exchange, request) -> {
                Map<String, Object> arguments = request.arguments();

                StringBuilder promptMessage = new StringBuilder("Please list files in this directory: ");
                promptMessage.append(arguments.get("directoryPath"));

                if (Boolean.TRUE.equals(arguments.get("recursive"))) {
                    promptMessage.append(", recursively");
                }

                final String pattern = arguments.getOrDefault("fileNamePattern", "").toString();
                if (Utils.hasText(pattern)) {
                    promptMessage.append(", with file name pattern: ").append(pattern);
                }

                McpSchema.TextContent content = new McpSchema.TextContent(promptMessage.toString());
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
