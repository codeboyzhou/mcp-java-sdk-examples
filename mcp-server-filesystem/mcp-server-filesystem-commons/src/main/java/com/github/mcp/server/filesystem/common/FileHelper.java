package com.github.mcp.server.filesystem.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class FileHelper {

	public static String readResourceAsString(String filename) throws IOException {
		try (InputStream inputStream = FileHelper.class.getClassLoader().getResourceAsStream(filename)) {
			if (inputStream == null) {
				throw new NoSuchFileException(filename);
			}
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			return bufferedReader.lines().collect(joining(System.lineSeparator()));
		}
	}

	public static String readAsString(Path filepath) throws IOException {
		return String.join(System.lineSeparator(), Files.readAllLines(filepath));
	}

	public static List<String> fuzzySearch(String start, String filename) throws IOException {
		if (SystemHelper.isWindows()) {
			return ShellHelper.execute("cmd.exe", "/c", "dir", start, "/s", "/b", "|", "findstr", "/i", filename);
		}
		if (SystemHelper.isLinux() || SystemHelper.isMacOS()) {
			return ShellHelper.execute("sh", "-c", "find", start, "-name", filename);
		}
		return List.of();
	}

	public static List<String> listDirectory(String dir) throws IOException {
		try (Stream<Path> stream = Files.list(Path.of(dir))) {
			return stream.map(Path::toAbsolutePath).map(Path::toString).toList();
		}
	}

}
