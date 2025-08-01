package com.github.mcp.server.filesystem;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;

/**
 * This class is used to define and implement some resources of the MCP server.
 *
 * @author codeboyzhou
 */
public final class McpResources {

	/**
	 * Create an MCP resource that provides access to the filesystem. Note that this is
	 * just an example that how to add resource to the MCP server, so there will be no
	 * real contents returned.
	 * @return The contents of the resource, wrapped as a
	 * {@link McpServerFeatures.SyncResourceSpecification} object.
	 */
	public static McpServerFeatures.SyncResourceSpecification filesystem() {
		// Step 1: Create a resource with URI, name, description, and MIME type.
		McpSchema.Annotations annotations = new McpSchema.Annotations(List.of(McpSchema.Role.ASSISTANT), 1.0);
		McpSchema.Resource resource = new McpSchema.Resource("file://system", "filesystem",
				"File system operations interface", "text/plain", annotations);

		// Step 2: Create a resource specification with the resource and a read handler.
		return new McpServerFeatures.SyncResourceSpecification(resource, (exchange, request) -> {
			// Step 3: Return the contents of the resource.
			McpSchema.ResourceContents contents = new McpSchema.TextResourceContents(resource.uri(),
					resource.mimeType(), "No real contents, just an example");
			return new McpSchema.ReadResourceResult(List.of(contents));
		});
	}

}
