package com.github.mcp.server.filesystem.declarative;

import com.github.codeboyzhou.mcp.declarative.annotation.McpResource;

/**
 * This class is used to define and implement some resources of the MCP server.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public final class Resources {
  /**
   * Create an MCP resource that provides access to the filesystem. Note that this is just an
   * example that how to add resource to the MCP server, so there will be no real contents returned.
   */
  @McpResource(
      uri = "file://system",
      name = "filesystem",
      title = "mcp.server.filesystem.resource.filesystem.title",
      description = "mcp.server.filesystem.resource.filesystem.description")
  public String filesystem() {
    return "No real contents, just an example";
  }
}
