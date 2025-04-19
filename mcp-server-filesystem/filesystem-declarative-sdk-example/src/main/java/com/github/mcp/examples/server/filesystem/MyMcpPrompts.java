package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompt;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPromptParam;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompts;

/**
 * A class that defines MCP prompts to the MCP server.
 *
 * @author codeboyzhou
 */
@McpPrompts
public class MyMcpPrompts {

    /**
     * Create a MCP prompt to read complete contents of a file.
     *
     * @param path The filepath to read, required.
     * @return "What is the complete contents of the file: /path/to/test.txt"
     */
    @McpPrompt(name = "read_file", description = "Read complete contents of a file.")
    public static String readFile(
        @McpPromptParam(name = "path", description = "The filepath to read, required.", required = true) String path
    ) {
        return String.format("What is the complete contents of the file: %s", path);
    }

    /**
     * Create a MCP prompt to list files of a directory.
     *
     * @param path      The directory path to list files, required.
     * @param pattern   The file name pattern to match, optional.
     * @param recursive Whether to list files recursively, optional.
     * @return "List files in the directory: /path/to/dir, with file name pattern: *.txt, recursively: true"
     */
    @McpPrompt(name = "list_files", description = "List files of a directory.")
    public static String listFiles(
        @McpPromptParam(name = "path", description = "The directory path to list files, required.", required = true) String path,
        @McpPromptParam(name = "pattern", description = "The file name pattern to match, optional.") String pattern,
        @McpPromptParam(name = "recursive", description = "Whether to list files recursively, optional.") boolean recursive
    ) {
        return String.format("List files in the directory: %s, with file name pattern: %s, recursively: %s",
            path, pattern, recursive
        );
    }

}
