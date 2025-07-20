package com.github.mcp.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.annotation.McpResource;
import com.github.codeboyzhou.mcp.declarative.annotation.McpResources;

/**
 * This class is used to define and implement some resources of the MCP server.
 *
 * @author codeboyzhou
 */
@McpResources
public class MyMcpResources {

	/**
	 * Create an MCP resource that provides access to the filesystem. Note that this is
	 * just an example that how to add resource to the MCP server, so there will be no
	 * real contents returned.
	 */
	@McpResource(uri = "file://system", name = "filesystem", descriptionI18nKey = "filesystem_resource_description")
	public String filesystem() {
		return "No real contents, just an example";
	}

}
