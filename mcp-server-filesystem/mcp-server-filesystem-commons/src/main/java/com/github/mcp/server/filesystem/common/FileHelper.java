package com.github.mcp.server.filesystem.common;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * A utility class that provides common file operations.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public final class FileHelper {
  /**
   * Reads a resource file as a string.
   *
   * @param filename the name of the resource file to read
   * @return the content of the resource file as a string
   * @throws IOException if an I/O error occurs or the resource file does not exist
   */
  public static String readResourceAsString(String filename) throws IOException {
    ClassLoader classLoader = FileHelper.class.getClassLoader();
    try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
      if (inputStream == null) {
        throw new NoSuchFileException(filename);
      }
      InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
      try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
        return bufferedReader.lines().collect(joining(System.lineSeparator()));
      }
    }
  }

  /**
   * Reads a file as a string.
   *
   * @param filepath the path of the file to read
   * @return the content of the file as a string
   * @throws IOException if an I/O error occurs while reading the file
   */
  public static String readAsString(Path filepath) throws IOException {
    return String.join(System.lineSeparator(), Files.readAllLines(filepath));
  }

  /**
   * Performs a fuzzy search for files with the given name starting from the specified directory. On
   * Windows, it uses 'dir' command with 'findstr'. On Linux/MacOS, it uses the 'find' command.
   *
   * @param start the starting directory for the search
   * @param filename the name of the file to search for (case-sensitive)
   * @return a list of file paths matching the search criteria
   * @throws IOException if an I/O error occurs while executing the search command
   */
  public static List<String> fuzzySearch(String start, String filename) throws IOException {
    final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
    if (isWindows) {
      return ShellHelper.exec("cmd.exe", "/c", "dir", start, "/s", "/b", "|", "findstr", filename);
    }
    return ShellHelper.exec("sh", "-c", "find", start, "-name", filename);
  }

  /**
   * Lists all files and directories in the specified directory.
   *
   * @param dir the directory to list
   * @return a list of absolute paths of all items in the directory
   * @throws IOException if an I/O error occurs while accessing the directory
   */
  public static List<String> listDirectory(String dir) throws IOException {
    try (Stream<Path> stream = Files.list(Path.of(dir))) {
      return stream.map(Path::toAbsolutePath).map(Path::toString).toList();
    }
  }
}
