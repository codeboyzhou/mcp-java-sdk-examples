package com.github.mcp.server.filesystem;

import com.github.mcp.server.filesystem.common.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class is used to define and implement some tools of the MCP server. Generally,
 * each {@code tool} method corresponds to a dedicated {@code prompt} method to ensure
 * that LLM applications can effectively utilize them through structured interactions.
 * This design pattern binds tool functionality descriptions and parameters with prompt
 * templates, enabling LLMs to understand tool purposes, parameter formats, and invocation
 * conditions during reasoning processes.
 *
 * @author codeboyzhou
 */
@Service
public class McpTools {

	private static final Logger log = LoggerFactory.getLogger(McpTools.class);

	/**
	 * Create an MCP tool to search for files or directories within the filesystem
	 * hierarchy starting from the specified start path. This method recursively traverses
	 * the directory structure beginning at the provided {@code start} path, identifying
	 * all entries (both files and directories) whose names contain the specified target
	 * {@code name}. The search is case-sensitive and matches partial names (e.g., "temp"
	 * would match "template.log").
	 * @return A list of absolute path strings for all matching entries found during the
	 * search.
	 */
	@Tool(description = "Start from the specified starting path and recursively search for sub-files or sub-directories.")
	public String find(@ToolParam(description = "The starting path to search, required.") String start, @ToolParam(
			description = "The name of the target file or directory to search, supports fuzzy matching, required.") String name) {

		if (start == null || start.isBlank()) {
			return "Please provide a valid start path to find.";
		}

		if (Files.notExists(Path.of(start))) {
			return "Start path does not exist: " + start + ", stopped finding.";
		}

		if (name == null || name.isBlank()) {
			return "Please provide a valid file/directory name to find.";
		}

		try {
			List<String> paths = FileHelper.fuzzySearch(start, name);
			if (paths.isEmpty()) {
				return String.format("No file (or directory) found with name '%s'", name);
			}
			else {
				return String.format("The following are the search results of name '%s': %s", name, paths);
			}
		}
		catch (IOException e) {
			final String result = String.format("Error searching file: %s, %s: %s", name, e, e.getMessage());
			log.error(result, e);
			return result;
		}
	}

	/**
	 * Create an MCP tool to read and return the content of a file or the list of
	 * immediate subdirectories and files within a directory from the filesystem. This
	 * method checks the type of the specified path: If the path points to a file, it
	 * reads the entire content of the file and returns it as a string. If the path points
	 * to a directory, it returns a list of strings representing the direct children
	 * (immediate subdirectories and files) directly under the specified directory
	 * (non-recursive).
	 * @param path The path to read, both file and directory are acceptable, required.
	 * @return If the path points to a file, it returns a string containing the file's
	 * content. If the path points to a directory, it returns a list of strings
	 * representing the direct children (immediate subdirectories and files) directly
	 * under the specified directory (non-recursive).
	 */
	@Tool(description = "Read the contents of a file or non-recursively read the sub-files and sub-directories under a directory.")
	public String read(
			@ToolParam(description = "The path to read, can be a file or directory, required.") String path) {

		if (path == null || path.isBlank()) {
			return "Please provide a valid path to read.";
		}

		Path filepath = Path.of(path);
		if (Files.notExists(filepath)) {
			return "The path does not exist: " + filepath + ", stopped reading.";
		}

		if (Files.isDirectory(filepath)) {
			try {
				List<String> paths = FileHelper.listDirectory(path);
				return String.format("The directory '%s' contains: %s", path, paths);
			}
			catch (IOException e) {
				final String result = String.format("Error reading directory: %s, %s: %s", path, e, e.getMessage());
				log.error(result, e);
				return result;
			}
		}

		try {
			return FileHelper.readAsString(filepath);
		}
		catch (IOException e) {
			final String result = String.format("Error reading file: %s, %s: %s", path, e, e.getMessage());
			log.error(result, e);
			return result;
		}
	}

	/**
	 * Create an MCP tool to delete a file or directory from the filesystem.
	 * @param path The path to delete, can be a file or directory, required.
	 * @return A message indicating whether the path was successfully deleted or not.
	 */
	@Tool(description = "Delete a file or directory from the filesystem.")
	public String delete(
			@ToolParam(description = "The path to delete, can be a file or directory, required.") String path) {

		if (path == null || path.isBlank()) {
			return "Please provide a valid path to delete.";
		}

		try {
			final boolean deleted = Files.deleteIfExists(Path.of(path));
			return (deleted ? "Successfully deleted path: " : "Failed to delete path: ") + path;
		}
		catch (IOException e) {
			final String result = String.format("Error deleting path: %s, %s: %s", path, e, e.getMessage());
			log.error(result, e);
			return result;
		}
	}

}
