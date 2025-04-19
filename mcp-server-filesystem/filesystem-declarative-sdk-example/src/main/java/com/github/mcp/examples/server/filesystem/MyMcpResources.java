package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.annotation.McpResource;
import com.github.codeboyzhou.mcp.declarative.annotation.McpResources;

/**
 * A class that defines resources to the MCP server.
 *
 * @author codeboyzhou
 */
@McpResources
public class MyMcpResources {

    /**
     * Create a MCP resource that provides access to the file system.
     * Note that this resource is just an example of how to add a resource to the MCP server.
     * So it's not really useful for anything other than demonstration purposes.
     *
     * @return A string that represents the resource.
     */
    @McpResource(uri = "file://system", name = "filesystem", description = "File system operations interface")
    public String filesystem() {
        return "No specific resource contents, just use the tools.";
    }

}
