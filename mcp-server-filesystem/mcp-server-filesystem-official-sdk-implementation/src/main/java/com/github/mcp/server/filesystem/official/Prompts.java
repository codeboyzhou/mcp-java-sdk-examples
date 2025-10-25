package com.github.mcp.server.filesystem.official;

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
    McpSchema.PromptArgument argumentStart =
        new McpSchema.PromptArgument(
            "start", "starting path", "The starting path to search, required.", true);
    McpSchema.PromptArgument argumentName =
        new McpSchema.PromptArgument(
            "name",
            "target file/dir name",
            "The name of the target file or dir to search, supports fuzzy matching, required.",
            true);

    // Step 2: Create a prompt with name, description, and arguments.
    List<McpSchema.PromptArgument> args = List.of(argumentStart, argumentName);
    McpSchema.Prompt prompt =
        new McpSchema.Prompt(
            "find",
            "file/dir search",
            "Start from the specified path and recursively search subitems.",
            args);

    // Step 3: Create a prompt specification with the prompt and the prompt handler.
    return new McpServerFeatures.SyncPromptSpecification(
        prompt,
        (exchange, request) -> {
          // Step 4: Create a prompt message with role and content.
          Map<String, Object> arguments = request.arguments();
          Object start = arguments.get(argumentStart.name());
          Object name = arguments.get(argumentName.name());

          if (start == null || start.toString().isBlank()) {
            return result(prompt, "Please provide a valid start path to find.");
          }

          if (name == null || name.toString().isBlank()) {
            return result(prompt, "Please provide a valid file/dir name to find.");
          }

          final String result =
              String.format(
                  "Call the MCP tool 'find' to search for files or dirs whose name matches: '%s', starting from the specified path: '%s'",
                  name, start);
          return result(prompt, result);
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
    McpSchema.PromptArgument argumentPath =
        new McpSchema.PromptArgument(
            "path",
            "target file/dir path",
            "The path to read, can be a file or dir, required.",
            true);

    // Step 2: Create a prompt with name, description, and arguments.
    List<McpSchema.PromptArgument> args = List.of(argumentPath);
    McpSchema.Prompt prompt =
        new McpSchema.Prompt(
            "read", "file/dir read", "Read a file or list dir contents non-recursively.", args);

    // Step 3: Create a prompt specification with the prompt and the prompt handler.
    return new McpServerFeatures.SyncPromptSpecification(
        prompt,
        (exchange, request) -> {
          // Step 4: Create a prompt message with role and content.
          Map<String, Object> arguments = request.arguments();
          Object path = arguments.get(argumentPath.name());

          if (path == null || path.toString().isBlank()) {
            return result(prompt, "Please provide a valid path to read.");
          }

          return result(prompt, "Call the MCP tool 'read' to read the file or dir: " + path);
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
    McpSchema.PromptArgument argumentPath =
        new McpSchema.PromptArgument(
            "path",
            "target file/dir path",
            "The path to delete, can be a file or dir, required.",
            true);

    // Step 2: Create a prompt with name, description, and arguments.
    List<McpSchema.PromptArgument> args = List.of(argumentPath);
    McpSchema.Prompt prompt =
        new McpSchema.Prompt(
            "delete", "file/dir delete", "Delete a file or dir from the filesystem.", args);

    // Step 3: Create a prompt specification with the prompt and the prompt handler.
    return new McpServerFeatures.SyncPromptSpecification(
        prompt,
        (exchange, request) -> {
          // Step 4: Create a prompt message with role and content.
          Map<String, Object> arguments = request.arguments();
          Object path = arguments.get(argumentPath.name());

          if (path == null || path.toString().isBlank()) {
            return result(prompt, "Please provide a valid path to delete.");
          }

          return result(prompt, "Call the MCP tool 'delete' to delete the file or dir: " + path);
        });
  }

  /**
   * Create a prompt result with the given prompt and result.
   *
   * @param prompt The prompt to use.
   * @param result The result to use.
   * @return The prompt result.
   */
  private static McpSchema.GetPromptResult result(McpSchema.Prompt prompt, String result) {
    McpSchema.TextContent content = new McpSchema.TextContent(result);
    McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
    return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
  }
}
