package com.github.mcp.server.filesystem.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ShellHelper {

  private static final Logger log = LoggerFactory.getLogger(ShellHelper.class);

  public static List<String> execute(String... command) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.redirectErrorStream(true);

    final String commandStatement = String.join(" ", command);
    log.info("Executing command: {}", commandStatement);

    Process process;
    try {
      process = processBuilder.start();
    } catch (IOException e) {
      log.error("Failed to execute command: {}", commandStatement, e);
      return List.of();
    }

    InputStream inputStream = process.getInputStream();
    InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
      process.waitFor(5, TimeUnit.MINUTES);
      return bufferedReader.lines().toList();
    } catch (InterruptedException e) {
      log.error("Failed to wait for command: {}", commandStatement, e);
      process.destroy();
      return List.of();
    }
  }
}
