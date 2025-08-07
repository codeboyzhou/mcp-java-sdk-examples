package com.github.mcp.server.filesystem.common;

public final class SystemHelper {

  private static final boolean WINDOWS =
      System.getProperty("os.name").toLowerCase().contains("win");

  private static final boolean LINUX =
      System.getProperty("os.name").toLowerCase().contains("linux");

  private static final boolean MAC_OS = System.getProperty("os.name").toLowerCase().contains("mac");

  public static boolean isWindows() {
    return WINDOWS;
  }

  public static boolean isLinux() {
    return LINUX;
  }

  public static boolean isMacOS() {
    return MAC_OS;
  }
}
