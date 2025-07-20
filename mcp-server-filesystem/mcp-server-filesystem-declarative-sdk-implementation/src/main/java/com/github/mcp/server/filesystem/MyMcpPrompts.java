package com.github.mcp.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompt;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPromptParam;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompts;

/**
 * This class is used to define and implement some prompts of the MCP server. Generally,
 * each {@code tool} method corresponds to a dedicated {@code prompt} method to ensure
 * that LLM applications can effectively utilize them through structured interactions.
 * This design pattern binds tool functionality descriptions and parameters with prompt
 * templates, enabling LLMs to understand tool purposes, parameter formats, and invocation
 * conditions during reasoning processes. However, some prompts may not require tools, in
 * which case they are simply standalone prompt messages. Such pure prompt messages are
 * suitable for scenarios without external system interactions, such as basic text
 * generation or simple logical reasoning tasks.
 *
 * @author codeboyzhou
 * @see MyMcpTools
 */
@McpPrompts
public class MyMcpPrompts {

	/**
	 * Create an MCP prompt to correspond with the {@code find} tool.
	 * @param start The starting path to search, required.
	 * @param name The file name to search, fuzzy search supported, required.
	 * @return The prompt message
	 * @see MyMcpTools#find(String, String)
	 */
	@McpPrompt(descriptionI18nKey = "description_for_find")
	public static String find(
			@McpPromptParam(name = "start", descriptionI18nKey = "description_for_find_param_start",
					required = true) String start,
			@McpPromptParam(name = "name", descriptionI18nKey = "description_for_find_param_name",
					required = true) String name) {

		if (start == null || start.isBlank()) {
			return "Please provide a valid start path to find.";
		}

		if (name == null || name.isBlank()) {
			return "Please provide a valid file/directory name to find.";
		}

		return String.format(
				"Call the MCP tool 'find' to search for files or directories whose name matches: '%s', starting from the specified start path: '%s'",
				name, start);
	}

	/**
	 * Create an MCP prompt to correspond with the {@code read} tool.
	 * @param path The path to read, can be a file or directory, required.
	 * @return The prompt message
	 * @see MyMcpTools#read(String)
	 */
	@McpPrompt(descriptionI18nKey = "description_for_read")
	public static String read(@McpPromptParam(name = "path", descriptionI18nKey = "description_for_read_param_path",
			required = true) String path) {

		if (path == null || path.isBlank()) {
			return "Please provide a valid path to read.";
		}
		return "Call the MCP tool 'read' to read the file or directory: " + path;
	}

	/**
	 * Create an MCP prompt to correspond with the {@code delete} tool.
	 * @param path The path to delete, can be a file or directory, required.
	 * @return The prompt message
	 * @see MyMcpTools#delete(String)
	 */
	@McpPrompt(descriptionI18nKey = "description_for_delete")
	public static String delete(@McpPromptParam(name = "path", descriptionI18nKey = "description_for_delete_param_path",
			required = true) String path) {

		if (path == null || path.isBlank()) {
			return "Please provide a valid path to delete.";
		}
		return "Call the MCP tool 'delete' to delete the file or directory: " + path;
	}

}
