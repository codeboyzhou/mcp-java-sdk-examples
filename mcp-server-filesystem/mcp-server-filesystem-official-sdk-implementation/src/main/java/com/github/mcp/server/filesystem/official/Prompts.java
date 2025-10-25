package com.github.mcp.server.filesystem.official;

import com.github.codeboyzhou.mcp.declarative.util.StringHelper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.Map;

/**
 * This class is used to define and implement some prompts of the MCP server. Generally, each {@code
 * tool} method corresponds to a dedicated {@code prompt} method to ensure that LLM applications can
 * effectively utilize them through structured interactions. This design pattern binds tool
 * functionality descriptions and parameters with prompt templates, enabling LLMs to understand tool
 * purposes, parameter formats, and invocation conditions during reasoning processes. However, some
 * prompts may not require tools, in which case they are simply standalone prompt messages. Such
 * pure prompt messages are suitable for scenarios without external system interactions, such as
 * basic text generation or simple logical reasoning tasks.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @see Tools
 */
public final class Prompts {
  /**
   * Create an MCP prompt to correspond with the {@code find} tool.
   *
   * @return The prompt message, wrapped as a {@link McpServerFeatures.SyncPromptSpecification}
   *     object.
   * @see Tools#find()
   */
  public static McpServerFeatures.SyncPromptSpecification find() {
    // Step 1: Create a prompt argument with name, description, and required flag.
    McpSchema.PromptArgument start =
        new McpSchema.PromptArgument("start", "The starting path to search, required.", true);
    McpSchema.PromptArgument name =
        new McpSchema.PromptArgument(
            "name",
            "The name of the target file or directory to search, supports fuzzy matching, required.",
            true);

    // Step 2: Create a prompt with name, description, and arguments.
    List<McpSchema.PromptArgument> args = List.of(start, name);
    McpSchema.Prompt prompt =
        new McpSchema.Prompt(
            "find", "Start from the specified path and recursively search subitems.", args);

    // Step 3: Create a prompt specification with the prompt and the prompt handler.
    return new McpServerFeatures.SyncPromptSpecification(
        prompt,
        (exchange, request) -> {
          // Step 4: Create a prompt message with role and content.
          Map<String, Object> arguments = request.arguments();
          final String startPath = arguments.getOrDefault("start", StringHelper.EMPTY).toString();
          final String nameToFind = arguments.getOrDefault("name", StringHelper.EMPTY).toString();
          String result;

          if (startPath.isBlank()) {
            result = "Please provide a valid start path to find.";
          } else if (nameToFind.isBlank()) {
            result = "Please provide a valid file/directory name to find.";
          } else {
            result =
                String.format(
                    "Call the MCP tool 'find' to search for files or directories whose name matches: '%s', starting from the specified start path: '%s'",
                    nameToFind, startPath);
          }

          McpSchema.TextContent content = new McpSchema.TextContent(result);
          McpSchema.PromptMessage message =
              new McpSchema.PromptMessage(McpSchema.Role.USER, content);
          return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
        });
  }

  /**
   * Create an MCP prompt to correspond with the {@code read} tool.
   *
   * @return The prompt message, wrapped as a {@link McpServerFeatures.SyncPromptSpecification}
   *     object.
   * @see Tools#read()
   */
  public static McpServerFeatures.SyncPromptSpecification read() {
    // Step 1: Create a prompt argument with name, description, and required flag.
    McpSchema.PromptArgument path =
        new McpSchema.PromptArgument(
            "path", "The path to read, can be a file or directory, required.", true);

    // Step 2: Create a prompt with name, description, and arguments.
    List<McpSchema.PromptArgument> args = List.of(path);
    McpSchema.Prompt prompt =
        new McpSchema.Prompt(
            "read", "Read a file or list directory contents non-recursively.", args);

    // Step 3: Create a prompt specification with the prompt and the prompt handler.
    return new McpServerFeatures.SyncPromptSpecification(
        prompt,
        (exchange, request) -> {
          // Step 4: Create a prompt message with role and content.
          Map<String, Object> arguments = request.arguments();
          final String filepath = arguments.getOrDefault("path", StringHelper.EMPTY).toString();
          String result;

          if (filepath.isBlank()) {
            result = "Please provide a valid path to read.";
          } else {
            result = "Call the MCP tool 'read' to read the file or directory: " + filepath;
          }

          McpSchema.TextContent content = new McpSchema.TextContent(result);
          McpSchema.PromptMessage message =
              new McpSchema.PromptMessage(McpSchema.Role.USER, content);
          return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
        });
  }

  /**
   * Create an MCP prompt to correspond with the {@code delete} tool.
   *
   * @return The prompt message, wrapped as a {@link McpServerFeatures.SyncPromptSpecification}
   *     object.
   * @see Tools#delete()
   */
  public static McpServerFeatures.SyncPromptSpecification delete() {
    // Step 1: Create a prompt argument with name, description, and required flag.
    McpSchema.PromptArgument path =
        new McpSchema.PromptArgument(
            "path", "The path to delete, can be a file or directory, required.", true);

    // Step 2: Create a prompt with name, description, and arguments.
    List<McpSchema.PromptArgument> args = List.of(path);
    McpSchema.Prompt prompt =
        new McpSchema.Prompt("delete", "Delete a file or directory from the filesystem.", args);

    // Step 3: Create a prompt specification with the prompt and the prompt handler.
    return new McpServerFeatures.SyncPromptSpecification(
        prompt,
        (exchange, request) -> {
          // Step 4: Create a prompt message with role and content.
          Map<String, Object> arguments = request.arguments();
          final String filepath = arguments.getOrDefault("path", StringHelper.EMPTY).toString();
          String result;

          if (filepath.isBlank()) {
            result = "Please provide a valid path to delete.";
          } else {
            result = "Call the MCP tool 'delete' to delete the file or directory: " + filepath;
          }

          McpSchema.TextContent content = new McpSchema.TextContent(result);
          McpSchema.PromptMessage message =
              new McpSchema.PromptMessage(McpSchema.Role.USER, content);
          return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
        });
  }
}
