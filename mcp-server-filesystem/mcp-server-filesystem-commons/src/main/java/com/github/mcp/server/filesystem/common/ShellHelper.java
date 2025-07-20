package com.github.mcp.server.filesystem.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class ShellHelper {

	private static final Logger log = LoggerFactory.getLogger(ShellHelper.class);

	public static List<String> execute(String... command) {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);

		final String commandStatement = String.join(" ", command);
		log.info("Executing command: {}", commandStatement);

		Process process;
		try {
			process = processBuilder.start();
		}
		catch (IOException e) {
			log.error("Failed to execute command: {}", commandStatement, e);
			return List.of();
		}

		InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		List<String> result = bufferedReader.lines().toList();

		try {
			process.waitFor(5, TimeUnit.MINUTES);
		}
		catch (InterruptedException e) {
			log.error("Failed to wait for command: {}", commandStatement, e);
			process.destroy();
		}

		return result;
	}

}
