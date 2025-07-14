package com.github.mcp.examples.server.filesystem;

import com.github.codeboyzhou.mcp.declarative.annotation.McpTool;
import com.github.codeboyzhou.mcp.declarative.annotation.McpToolParam;
import com.github.codeboyzhou.mcp.declarative.annotation.McpTools;
import com.github.mcp.examples.server.filesystem.util.FileAccessHelper;
import com.github.mcp.examples.server.filesystem.util.FileOperationHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A class that defines tools to the MCP server.
 *
 * @author codeboyzhou
 */
@McpTools
public class MyMcpTools {

    /**
     * Create a MCP tool to read complete contents of a file.
     *
     * @param path The filepath to read, required.
     * @return The contents of the file, or an error message if the file cannot be read.
     * @throws IOException If there is an error reading the file.
     */
    @McpTool(name = "read_file", descriptionI18nKey = "read_file_tool_description")
    public String readFile(
        @McpToolParam(name = "path", descriptionI18nKey = "read_file_tool_param_path_description", required = true) String path
    ) throws IOException {
        Path filepath = Path.of(path);

        if (Files.notExists(filepath)) {
            return String.format("%s does not exist. No content available.", path);
        }

        if (Files.isDirectory(filepath)) {
            return String.format("%s is a directory. No content available.", path);
        }

        if (FileAccessHelper.checkReadableConfiguration(filepath)) {
            return FileOperationHelper.readAsString(filepath);
        }

        return String.format("Access to %s is denied. No content available.", path);
    }

    /**
     * Create a MCP tool to list files of a directory.
     *
     * @param path      The directory path to read, required.
     * @param pattern   Regular expression to filter files, optional, default is {@code ""}, means no filter.
     * @param recursive Whether to list files recursively, optional, default is {@code false}.
     * @return A list of file names (paths if 'recursive' is {@code true}), or empty string if no files found.
     * @throws IOException If there is an error reading the directory.
     */
    @McpTool(name = "list_files", descriptionI18nKey = "list_files_tool_description")
    public String listFiles(
        @McpToolParam(name = "path", descriptionI18nKey = "list_files_tool_param_path_description", required = true) String path,
        @McpToolParam(name = "pattern", descriptionI18nKey = "list_files_tool_param_pattern_description") String pattern,
        @McpToolParam(name = "recursive", descriptionI18nKey = "list_files_tool_param_recursive_description") boolean recursive
    ) throws IOException {
        Path dirpath = Path.of(path);

        if (Files.notExists(dirpath)) {
            return String.format("%s does not exist. No files available.", path);
        }

        if (Files.isRegularFile(dirpath)) {
            return String.format("%s is not a directory. No files available.", path);
        }

        if (FileAccessHelper.checkReadableConfiguration(dirpath)) {
            List<String> filenames = FileOperationHelper.listFiles(dirpath, pattern, recursive);
            if (filenames.isEmpty()) {
                return "Target file not found in this directory: " + path;
            }
            return String.join(System.lineSeparator(), filenames);
        }

        return String.format("Access to %s is denied. No files available.", path);
    }

}
