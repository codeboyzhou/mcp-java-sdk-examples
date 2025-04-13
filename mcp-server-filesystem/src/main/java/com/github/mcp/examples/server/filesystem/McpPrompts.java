package com.github.mcp.examples.server.filesystem;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

/**
 * A class that defines MCP prompts to the MCP server.
 *
 * @author codeboyzhou
 */
public class McpPrompts {

    /**
     * Create a MCP prompt to read complete contents of a file.
     * --------------------------------------------------------
     * Prompt Arguments:
     * path (string): The filepath to read, required.
     * --------------------------------------------------------
     * Prompt Return:
     * "What is the complete contents of the file: /path/to/file.txt"
     *
     * @return {@link McpServerFeatures.SyncPromptSpecification}
     */
    public static McpServerFeatures.SyncPromptSpecification readFile() {
        // Step 1: Create a prompt argument with name, description, and required flag.
        McpSchema.PromptArgument path = new McpSchema.PromptArgument("path", "The filepath to read, required.", true);

        // Step 2: Create a prompt with name, description, and arguments.
        McpSchema.Prompt prompt = new McpSchema.Prompt("read_file", "Read complete contents of a file.", List.of(path));

        // Step 3: Create a prompt specification with the prompt and the prompt handler.
        return new McpServerFeatures.SyncPromptSpecification(prompt, (exchange, request) -> {
            // Step 4: Create a prompt message with role and content.
            Map<String, Object> arguments = request.arguments();
            final String filepath = arguments.get("path").toString();
            McpSchema.TextContent content = new McpSchema.TextContent(
                String.format("What is the complete contents of the file: %s", filepath)
            );
            McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
            return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
        });
    }

    /**
     * Create a MCP prompt to list files of a directory.
     * -------------------------------------------------
     * Prompt Arguments:
     * path (string): The directory path to list files, required.
     * pattern (string): The file name pattern to match, optional, default is {@code ""}.
     * recursive (boolean): Whether to list files recursively, optional, default is {@code false}.
     * -------------------------------------------------
     * Prompt Return:
     * "List files in the directory: /path/to/directory, with file name pattern: *.txt, recursively: true"
     *
     * @return {@link McpServerFeatures.SyncPromptSpecification}
     */
    public static McpServerFeatures.SyncPromptSpecification listFiles() {
        // Step 1: Create a prompt argument with name, description, and required flag.
        McpSchema.PromptArgument path = new McpSchema.PromptArgument("path", "The directory path to list files, required.", true);
        McpSchema.PromptArgument pattern = new McpSchema.PromptArgument("pattern", "The file name pattern to match, optional.", false);
        McpSchema.PromptArgument recursive = new McpSchema.PromptArgument("recursive", "Whether to list files recursively, optional.", false);

        // Step 2: Create a prompt with name, description, and arguments.
        McpSchema.Prompt prompt = new McpSchema.Prompt("list_files", "List files of a directory.", List.of(path, pattern, recursive));

        // Step 3: Create a prompt specification with the prompt and the prompt handler.
        return new McpServerFeatures.SyncPromptSpecification(prompt, (exchange, request) -> {
            // Step 4: Create a prompt message with role and content.
            Map<String, Object> arguments = request.arguments();
            McpSchema.TextContent content = new McpSchema.TextContent(
                String.format("List files in the directory: %s, with file name pattern: %s, recursively: %s",
                    arguments.get("path"), arguments.getOrDefault("pattern", ""), arguments.getOrDefault("recursive", false)
                )
            );
            McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
            return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
        });
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
