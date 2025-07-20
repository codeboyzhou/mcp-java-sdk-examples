package com.github.mcp.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.McpServers;

/**
 * Java server implementing Model Context Protocol (MCP) for filesystem operations.
 *
 * @author codeboyzhou
 */
public class McpSseServer {

	/**
	 * Main entry for the HTTP SSE MCP server.
	 */
	public static void main(String[] args) {
		McpServers.run(McpSseServer.class, args).startServer();
	}

}
