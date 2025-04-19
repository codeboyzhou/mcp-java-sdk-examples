package com.github.mcp.examples.server.filesystem;

import com.github.mcp.examples.server.filesystem.util.FileAccessHelper;
import com.github.mcp.examples.server.filesystem.util.FileOperationHelper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A class that defines tools to the MCP server.
 *
 * @author codeboyzhou
 */
public final class McpTools {

    /**
     * Create a MCP tool to read complete contents of a file.
     * ------------------------------------------------------
     * Tool Arguments:
     * path (string): The filepath to read, required.
     * ------------------------------------------------------
     * Tool Return:
     * result (string): The contents of the file, or an error message if the file cannot be read.
     *
     * @return {@link McpServerFeatures.SyncToolSpecification}
     * @throws IOException If there is an error reading the file.
     */
    public static McpServerFeatures.SyncToolSpecification readFile() throws IOException {
        // Step 1: Load the JSON schema for the tool input arguments.
        final String schema = FileOperationHelper.readResourceAsString("schema/read-file-tool-input-json-schema.json");

        // Step 2: Create a tool with name, description, and JSON schema.
        McpSchema.Tool tool = new McpSchema.Tool("read_file", "Read complete contents of a file.", schema);

        // Step 3: Create a tool specification with the tool and the call function.
        return new McpServerFeatures.SyncToolSpecification(tool, (exchange, arguments) -> {
            // Step 4: Read the file and return the result.
            final String path = arguments.get("path").toString();
            Path filepath = Path.of(path);
            boolean isError = false;
            String result;

            if (Files.notExists(filepath)) {
                result = String.format("%s does not exist. No content available.", path);
            } else if (Files.isDirectory(filepath)) {
                result = String.format("%s is a directory. No content available.", path);
            } else if (FileAccessHelper.checkReadableConfiguration(filepath)) {
                try {
                    result = FileOperationHelper.readAsString(filepath);
                } catch (IOException e) {
                    isError = true;
                    result = e + ": " + e.getMessage();
                    System.err.println("Error reading file: " + path);
                    e.printStackTrace(System.err);
                }
            } else {
                result = String.format("Access to %s is denied. No content available.", path);
            }

            McpSchema.Content content = new McpSchema.TextContent(result);
            return new McpSchema.CallToolResult(List.of(content), isError);
        });
    }

    /**
     * Create a MCP tool to list files of a directory.
     * -----------------------------------------------
     * Tool Arguments:
     * path (string): The directory path to read, required.
     * pattern (string): Regular expression to filter files, optional, default is {@code ""}, means no filter.
     * recursive (boolean): Whether to list files recursively, optional, default is {@code false}.
     * -----------------------------------------------
     * Tool Return:
     * result (string): A list of file names (paths if 'recursive' is {@code true}), or empty string if no files found.
     *
     * @return {@link McpServerFeatures.SyncToolSpecification}
     * @throws IOException If there is an error reading the directory.
     */
    public static McpServerFeatures.SyncToolSpecification listFiles() throws IOException {
        // Step 1: Load the JSON schema for the tool input arguments.
        final String schema = FileOperationHelper.readResourceAsString("schema/list-files-tool-input-json-schema.json");

        // Step 2: Create a tool with name, description, and JSON schema.
        McpSchema.Tool tool = new McpSchema.Tool("list_files", "List files of a directory.", schema);

        // Step 3: Create a tool specification with the tool and the call function.
        return new McpServerFeatures.SyncToolSpecification(tool, (exchange, arguments) -> {
            // Step 4: List files and return the result.
            final String path = arguments.get("path").toString();
            final String pattern = arguments.getOrDefault("pattern", "").toString();
            final boolean recursive = arguments.getOrDefault("recursive", false).equals(true);
            Path dirpath = Path.of(path);
            boolean isError = false;
            String result;

            if (Files.notExists(dirpath)) {
                result = String.format("%s does not exist. No files available.", path);
            } else if (Files.isRegularFile(dirpath)) {
                result = String.format("%s is not a directory. No files available.", path);
            } else if (FileAccessHelper.checkReadableConfiguration(dirpath)) {
                try {
                    List<String> filenames = FileOperationHelper.listFiles(dirpath, pattern, recursive);
                    if (filenames.isEmpty()) {
                        result = "Target files not found in this directory: " + path;
                    } else {
                        result = String.join(System.lineSeparator(), filenames);
                    }
                } catch (IOException e) {
                    isError = true;
                    result = e + ": " + e.getMessage();
                    System.err.println("Error reading directory: " + path);
                    e.printStackTrace(System.err);
                }
            } else {
                result = String.format("Access to %s is denied. No files available.", path);
            }

            McpSchema.Content content = new McpSchema.TextContent(result);
            return new McpSchema.CallToolResult(List.of(content), isError);
        });
    }

    /**
     * Add all tools to the MCP server.
     *
     * @param server The MCP server to add tools to.
     */
    public static void addAllTo(McpSyncServer server) {
        try {
            server.addTool(readFile());
            server.addTool(listFiles());
        } catch (IOException e) {
            // We are in STDIO mode, so logging is unavailable and messages are output to STDERR only
            System.err.println("Error adding tools");
            e.printStackTrace(System.err);
        }
    }

}
