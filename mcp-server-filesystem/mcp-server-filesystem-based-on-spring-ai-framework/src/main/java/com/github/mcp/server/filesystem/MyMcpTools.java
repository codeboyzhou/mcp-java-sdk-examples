package com.github.mcp.server.filesystem;

import com.github.mcp.server.filesystem.util.FileAccessHelper;
import com.github.mcp.server.filesystem.util.FileOperationHelper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A class that defines tools to the MCP server.
 *
 * @author codeboyzhou
 */
@Service
public class MyMcpTools {

    /**
     * Create a MCP tool to read complete contents of a file.
     *
     * @param path The filepath to read, required.
     * @return The contents of the file, or an error message if the file cannot be read.
     * @throws IOException If there is an error reading the file.
     */
    @Tool(name = "read_file", description = "Read complete contents of a file")
    public String readFile(@ToolParam(description = "The filepath to read, required") String path) throws IOException {
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
    @Tool(name = "list_files", description = "List files of a directory")
    public String listFiles(
        @ToolParam(description = "The directory path to list files, required") String path,
        @ToolParam(description = "The file name pattern to match, optional", required = false) String pattern,
        @ToolParam(description = "Whether to list files recursively, optional", required = false) boolean recursive
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
