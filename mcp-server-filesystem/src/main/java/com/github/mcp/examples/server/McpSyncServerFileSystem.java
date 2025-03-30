package com.github.mcp.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpSyncServerFileSystem {
    /**
     * The name of the MCP server.
     */
    private static final String SERVER_NAME = "mcp-server-filesystem";

    /**
     * The version of the MCP server.
     */
    private static final String SERVER_VERSION = "0.1.0";

    /**
     * The MCP sync server instance.
     */
    private McpSyncServer server;

    /**
     * Initializes the MCP server on STDIO.
     */
    private void initialize() {
        McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
            .tools(true)
            .prompts(true)
            .resources(false, true)
            .build();

        server = McpServer.sync(new StdioServerTransportProvider())
            .serverInfo(SERVER_NAME, SERVER_VERSION)
            .capabilities(serverCapabilities)
            .build();

        // We are in STDIO mode, so logging is unavailable and messages are output to STDERR only
        System.err.println(SERVER_NAME + " " + SERVER_VERSION + " initialized in STDIO mode");
    }

    /**
     * Adds a resource to the MCP server that provides access to the file system.
     * Please note that this is just a simple example and does not provide any actual file system access.
     */
    public void addFileSystemResource() {
        McpSchema.Resource resource = new McpSchema.Resource(
            "file://system",
            "filesystem",
            "File system operations interface",
            "text/plain",
            new McpSchema.Annotations(List.of(McpSchema.Role.ASSISTANT, McpSchema.Role.USER), 1.0)
        );

        System.err.println("Adding resource: " + resource.uri());

        McpServerFeatures.SyncResourceSpecification resourceSpec = new McpServerFeatures.SyncResourceSpecification(
            resource,
            (exchange, request) -> {
                McpSchema.ResourceContents contents = new McpSchema.TextResourceContents(
                    resource.uri(), resource.mimeType(), "No specific resource contents, just use the tools."
                );
                return new McpSchema.ReadResourceResult(List.of(contents));
            }
        );

        server.addResource(resourceSpec);

        System.err.println("Resource added: " + resource.uri());
    }

    /**
     * Adds a tool to the MCP server that reads the contents of a file.
     * <p>
     * Arguments:
     * path (string): The path to the file to read.
     * <p>
     * Returns:
     * result (string): The contents of the file.
     * If the file does not exist or is a directory, an error message is returned.
     *
     * @throws IOException if there is an error reading the file.
     */
    private void addFileReadingTool() throws IOException {
        System.err.println("Adding tool: read_file");

        final String schema = FileHelper.readAsString("file-reading-tool-input-json-schema.json");
        McpSchema.Tool tool = new McpSchema.Tool("read_file", "Read complete contents of a file.", schema);
        McpServerFeatures.SyncToolSpecification fileReadingTool = new McpServerFeatures.SyncToolSpecification(
            tool,
            (exchange, arguments) -> {
                final String path = arguments.get("path").toString();
                Path filepath = Path.of(path);
                boolean isError = false;
                String result;

                if (Files.notExists(filepath)) {
                    result = String.format("No content available because %s does not exist", path);
                } else if (Files.isDirectory(filepath)) {
                    result = String.format("No content available because %s is a directory", path);
                } else {
                    try {
                        result = FileHelper.readAsString(path);
                    } catch (IOException e) {
                        result = e.getMessage();
                        isError = true;
                    }
                }

                McpSchema.Content content = new McpSchema.TextContent(result);
                return new McpSchema.CallToolResult(List.of(content), isError);
            }
        );


        server.addTool(fileReadingTool);

        System.err.println("Tool added: " + tool.name());
    }

    /**
     * Main entry point for the MCP server on STDIO.
     */
    public static void main(String[] args) {
        // Initialize MCP server
        McpSyncServerFileSystem filesystemMcpServer = new McpSyncServerFileSystem();
        filesystemMcpServer.initialize();
        // Add MCP server resources
        filesystemMcpServer.addFileSystemResource();
        // Add MCP server tools
        try {
            filesystemMcpServer.addFileReadingTool();
        } catch (IOException e) {
            // We are in STDIO mode, so logging is unavailable and messages are output to STDERR only
            System.err.println("Error calling addFileReadingTool()");
            e.printStackTrace(System.err);
        }
    }

}
