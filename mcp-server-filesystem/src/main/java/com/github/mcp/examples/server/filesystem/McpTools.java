package com.github.mcp.examples.server.filesystem;

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
     * Create a new tool to the MCP server that reads the contents of a file.
     * <p>
     * Arguments:
     * path (string): The path to the file to read.
     * <p>
     * Returns:
     * result (string): The contents of the file.
     * If the file does not exist or is a directory, an error message is returned.
     *
     * @return A specification for the MCP tool.
     * @throws IOException if there is an error reading the file.
     */
    public static McpServerFeatures.SyncToolSpecification readFile() throws IOException {
        final String schema = FileHelper.readResourceAsString("read-file-tool-input-json-schema.json");

        McpSchema.Tool tool = new McpSchema
            .Tool("read_file", "Read complete contents of a file.", schema);

        return new McpServerFeatures.SyncToolSpecification(
            tool,
            (exchange, arguments) -> {
                final String path = arguments.get("path").toString();
                Path filepath = Path.of(path);
                boolean isError = false;
                String result;

                if (Files.notExists(filepath)) {
                    result = String.format("%s does not exist. No content available.", path);
                } else if (Files.isDirectory(filepath)) {
                    result = String.format("%s is a directory. No content available.", path);
                } else if (FileHelper.AccessControl.checkReadableConfiguration(filepath)) {
                    try {
                        result = FileHelper.readAsString(filepath);
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
            }
        );
    }

    /**
     * Create a new tool to the MCP server that list files of a directory with name-based filtering.
     * <p>
     * Arguments:
     * directoryPath (string): The path to the directory to read.
     * fileNamePattern (string): Regular expression to filter files.
     * <p>
     * Returns:
     * result (string): A list of file names.
     *
     * @return A specification for the MCP tool.
     * @throws IOException if there is an error reading the directory.
     */
    public static McpServerFeatures.SyncToolSpecification listFiles() throws IOException {
        final String schema = FileHelper.readResourceAsString("list-files-tool-input-json-schema.json");

        McpSchema.Tool tool = new McpSchema
            .Tool("list_files", "List files of a directory with name-based filtering.", schema);

        return new McpServerFeatures.SyncToolSpecification(
            tool,
            (exchange, arguments) -> {
                final String directoryPath = arguments.get("directoryPath").toString();
                final String fileNamePattern = arguments.getOrDefault("fileNamePattern", "").toString();
                Path dirpath = Path.of(directoryPath);
                boolean isError = false;
                String result;

                if (Files.notExists(dirpath)) {
                    result = String.format("%s does not exist. No files available.", directoryPath);
                } else if (Files.isRegularFile(dirpath)) {
                    result = String.format("%s is not a directory. No files available.", directoryPath);
                } else if (FileHelper.AccessControl.checkReadableConfiguration(dirpath)) {
                    try {
                        List<String> filenames = FileHelper.listFiles(dirpath, fileNamePattern);
                        result = String.join(System.lineSeparator(), filenames);
                    } catch (IOException e) {
                        isError = true;
                        result = e + ": " + e.getMessage();
                        System.err.println("Error reading directory: " + directoryPath);
                        e.printStackTrace(System.err);
                    }
                } else {
                    result = String.format("Access to %s is denied. No files available.", directoryPath);
                }

                McpSchema.Content content = new McpSchema.TextContent(result);
                return new McpSchema.CallToolResult(List.of(content), isError);
            }
        );
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
