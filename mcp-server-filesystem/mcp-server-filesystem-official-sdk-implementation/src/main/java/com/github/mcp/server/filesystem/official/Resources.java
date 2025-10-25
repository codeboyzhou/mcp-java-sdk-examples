package com.github.mcp.server.filesystem.official;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;

/**
 * This class is used to define and implement some resources of the MCP server. It provides methods
 * to create and configure MCP resources that can be accessed by clients.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public final class Resources {
  /**
   * Creates an MCP resource that provides access to the filesystem. Note that this is just an
   * example of how to add a resource to the MCP server, so there will be no real contents returned.
   *
   * <p>This method creates a resource specification with the following properties:
   *
   * <ul>
   *   <li>URI: file://system
   *   <li>Name: filesystem
   *   <li>Description: File system operations interface
   *   <li>MIME type: text/plain
   *   <li>Annotations: Assistant role with priority 1.0
   * </ul>
   *
   * <p>The resource includes a read handler that returns example content.
   *
   * @return The contents of the resource, wrapped as a {@link
   *     McpServerFeatures.SyncResourceSpecification} object.
   */
  public static McpServerFeatures.SyncResourceSpecification filesystem() {
    // Step 1: Create a resource with URI, name, description, and MIME type.
    McpSchema.Resource resource =
        McpSchema.Resource.builder()
            .uri("file://system")
            .name("filesystem")
            .description("File system operations interface")
            .mimeType("text/plain")
            .annotations(new McpSchema.Annotations(List.of(McpSchema.Role.ASSISTANT), 1.0))
            .build();

    // Step 2: Create a resource specification with the resource and a read handler.
    return new McpServerFeatures.SyncResourceSpecification(
        resource,
        (exchange, request) -> {
          // Step 3: Return the contents of the resource.
          return text(resource, "No real contents, just an example");
        });
  }

  /**
   * Creates a {@link McpSchema.ReadResourceResult} object with the given resource and text
   * contents.
   *
   * @param resource The resource to include in the result.
   * @param text The text contents to include in the result.
   * @return A {@link McpSchema.ReadResourceResult} object with the given resource and text
   *     contents.
   */
  private static McpSchema.ReadResourceResult text(McpSchema.Resource resource, String text) {
    final String uri = resource.uri();
    final String mimeType = resource.mimeType();
    McpSchema.TextResourceContents contents =
        new McpSchema.TextResourceContents(uri, mimeType, text);
    return new McpSchema.ReadResourceResult(List.of(contents));
  }
}
