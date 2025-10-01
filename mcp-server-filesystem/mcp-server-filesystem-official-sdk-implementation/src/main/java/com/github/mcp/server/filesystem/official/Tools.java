package com.github.mcp.server.filesystem.official;

import com.github.codeboyzhou.mcp.declarative.util.StringHelper;
import com.github.mcp.server.filesystem.common.FileHelper;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to define and implement some tools of the MCP server. Generally, each {@code
 * tool} corresponds to a dedicated {@code prompt} to ensure that LLM applications can effectively
 * utilize them through structured interactions. This design pattern binds tool functionality
 * descriptions and parameters with prompt templates, enabling LLMs to understand tool purposes,
 * parameter formats, and invocation conditions during reasoning processes.
 *
 * @author codeboyzhou
 * @see Prompts
 */
public final class Tools {

  private static final Logger log = LoggerFactory.getLogger(Tools.class);

  /**
   * Create an MCP tool to search for files or directories within the filesystem starting from the
   * specified {@code start} path. This method recursively traverses the directory beginning at the
   * provided {@code start} path, identifying all entries (both files and directories) whose names
   * contain the specified target {@code name}. The search is case-sensitive and matches partial
   * names (e.g., "temp" would match "template.log").
   *
   * @return A list of absolute path strings for all matching entries found during the search,
   *     wrapped as a {@link McpServerFeatures.SyncToolSpecification} object.
   * @throws IOException If an I/O error occurs during filesystem traversal
   */
  public static McpServerFeatures.SyncToolSpecification find() throws IOException {
    // Step 1: Load the JSON schema for the tool input arguments.
    final String schema = FileHelper.readResourceAsString("schema/find.json");

    // Step 2: Create a tool with name, description, and JSON schema.
    McpSchema.Tool tool =
        McpSchema.Tool.builder()
            .name("find")
            .description("Start from the specified path and recursively search subitems.")
            .inputSchema(McpJsonMapper.getDefault(), schema)
            .build();

    // Step 3: Create a tool specification with the tool and the call function.
    return McpServerFeatures.SyncToolSpecification.builder()
        .tool(tool)
        .callHandler(
            (exchange, request) -> {
              // Step 4: List files and return the result.
              Map<String, Object> arguments = request.arguments();
              final String start = arguments.getOrDefault("start", StringHelper.EMPTY).toString();
              final String name = arguments.getOrDefault("name", StringHelper.EMPTY).toString();
              boolean error = false;
              String result;

              if (start.isBlank()) {
                result = "Please provide a valid start path to find.";
              } else if (Files.notExists(Path.of(start))) {
                result = "Start path does not exist: " + start + ", stopped finding.";
              } else if (name.isBlank()) {
                result = "Please provide a valid file/directory name to find.";
              } else {
                try {
                  List<String> paths = FileHelper.fuzzySearch(start, name);
                  if (paths.isEmpty()) {
                    result = String.format("No file (or directory) found with name '%s'", name);
                  } else {
                    result =
                        String.format(
                            "The following are the search results of name '%s': %s", name, paths);
                  }
                } catch (IOException e) {
                  error = true;
                  result =
                      String.format("Error searching file: %s, %s: %s", name, e, e.getMessage());
                  log.error(result, e);
                }
              }

              McpSchema.Content content = new McpSchema.TextContent(result);
              return McpSchema.CallToolResult.builder()
                  .content(List.of(content))
                  .isError(error)
                  .build();
            })
        .build();
  }

  /**
   * Create an MCP tool to read and return the content of a file or the list of immediate
   * subdirectories and files within a directory from the filesystem. This method checks the type of
   * the specified path: If the path points to a file, it reads the entire content of the file and
   * returns it as a string. If the path points to a directory, it returns a list of strings
   * representing the direct children (immediate subdirectories and files) directly under the
   * specified directory (non-recursive).
   *
   * @return If the path points to a file, it returns a string containing the file's content. If the
   *     path points to a directory, it returns a list of strings representing the direct children
   *     (immediate subdirectories and files) directly under the specified directory
   *     (non-recursive), wrapped as a {@link McpServerFeatures.SyncToolSpecification} object.
   * @throws IOException If an I/O error occurs during reading.
   */
  public static McpServerFeatures.SyncToolSpecification read() throws IOException {
    // Step 1: Load the JSON schema for the tool input arguments.
    final String schema = FileHelper.readResourceAsString("schema/read.json");

    // Step 2: Create a tool with name, description, and JSON schema.
    McpSchema.Tool tool =
        McpSchema.Tool.builder()
            .name("read")
            .description("Read a file or list directory contents non-recursively.")
            .inputSchema(McpJsonMapper.getDefault(), schema)
            .build();

    // Step 3: Create a tool specification with the tool and the call function.
    return McpServerFeatures.SyncToolSpecification.builder()
        .tool(tool)
        .callHandler(
            (exchange, request) -> {
              // Step 4: Read the path and return the result.
              Map<String, Object> arguments = request.arguments();
              final String path = arguments.getOrDefault("path", StringHelper.EMPTY).toString();
              boolean error = false;
              String result;

              if (path.isBlank()) {
                result = "Please provide a valid path to read.";
              } else {
                Path filepath = Path.of(path);
                if (Files.notExists(filepath)) {
                  result = "The path does not exist: " + filepath + ", stopped reading.";
                } else if (Files.isDirectory(filepath)) {
                  try {
                    List<String> paths = FileHelper.listDirectory(path);
                    result = String.format("The directory '%s' contains: %s", path, paths);
                  } catch (IOException e) {
                    error = true;
                    result =
                        String.format(
                            "Error reading directory: %s, %s: %s", path, e, e.getMessage());
                    log.error(result, e);
                  }
                } else {
                  try {
                    result = FileHelper.readAsString(filepath);
                  } catch (IOException e) {
                    error = true;
                    result =
                        String.format("Error reading file: %s, %s: %s", path, e, e.getMessage());
                    log.error(result, e);
                  }
                }
              }

              McpSchema.Content content = new McpSchema.TextContent(result);
              return McpSchema.CallToolResult.builder()
                  .content(List.of(content))
                  .isError(error)
                  .build();
            })
        .build();
  }

  /**
   * Create an MCP tool to delete a file or directory from the filesystem.
   *
   * @return The operation result, wrapped as a {@link McpServerFeatures.SyncToolSpecification}
   *     object.
   * @throws IOException If an I/O error occurs during deletion.
   */
  public static McpServerFeatures.SyncToolSpecification delete() throws IOException {
    // Step 1: Load the JSON schema for the tool input arguments.
    final String schema = FileHelper.readResourceAsString("schema/delete.json");

    // Step 2: Create a tool with name, description, and JSON schema.
    McpSchema.Tool tool =
        McpSchema.Tool.builder()
            .name("delete")
            .description("Delete a file or directory from the filesystem.")
            .inputSchema(McpJsonMapper.getDefault(), schema)
            .build();

    // Step 3: Create a tool specification with the tool and the call function.
    return McpServerFeatures.SyncToolSpecification.builder()
        .tool(tool)
        .callHandler(
            (exchange, request) -> {
              // Step 4: Delete the path and return the result.
              Map<String, Object> arguments = request.arguments();
              final String path = arguments.getOrDefault("path", StringHelper.EMPTY).toString();
              boolean error = false;
              String result;

              if (path.isBlank()) {
                result = "Please provide a valid path to delete.";
              } else {
                try {
                  final boolean deleted = Files.deleteIfExists(Path.of(path));
                  result = (deleted ? "Successfully" : "Failed to") + " deleted path: " + path;
                } catch (IOException e) {
                  error = true;
                  result =
                      String.format("Error deleting path: %s, %s: %s", path, e, e.getMessage());
                  log.error(result, e);
                }
              }

              McpSchema.Content content = new McpSchema.TextContent(result);
              return McpSchema.CallToolResult.builder()
                  .content(List.of(content))
                  .isError(error)
                  .build();
            })
        .build();
  }
}
