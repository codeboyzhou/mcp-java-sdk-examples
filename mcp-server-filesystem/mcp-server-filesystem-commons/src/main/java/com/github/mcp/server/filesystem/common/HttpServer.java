package com.github.mcp.server.filesystem.common;

import jakarta.servlet.http.HttpServlet;
import java.time.Duration;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

  private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

  private static final String DEFAULT_SERVLET_CONTEXT_PATH = "/";

  private static final String DEFAULT_SERVLET_PATH = "/*";

  private HttpServlet servlet;

  private int port;

  public HttpServer use(HttpServlet servlet) {
    this.servlet = servlet;
    return this;
  }

  public HttpServer bind(int port) {
    this.port = port;
    return this;
  }

  public void start() {
    ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    handler.setContextPath(DEFAULT_SERVLET_CONTEXT_PATH);

    ServletHolder servletHolder = new ServletHolder(servlet);
    handler.addServlet(servletHolder, DEFAULT_SERVLET_PATH);

    Server httpserver = new Server(port);
    httpserver.setHandler(handler);
    httpserver.setStopAtShutdown(true);
    httpserver.setStopTimeout(Duration.ofSeconds(5).getSeconds());

    try {
      httpserver.start();
      addShutdownHook(httpserver);
      log.info("Jetty-based HTTP server started on http://127.0.0.1:{}", port);
    } catch (Exception e) {
      log.error("Error starting HTTP server on http://127.0.0.1:{}", port, e);
    }

    try {
      httpserver.join();
    } catch (InterruptedException e) {
      log.error("Error joining HTTP server", e);
    }
  }

  private void addShutdownHook(Server httpserver) {
    Runnable runnable =
        () -> {
          try {
            log.info("Shutting down HTTP server and MCP server");
            httpserver.stop();
          } catch (Exception e) {
            log.error("Error stopping HTTP server and MCP server", e);
          }
        };
    Thread shutdownHook = new Thread(runnable);
    Runtime.getRuntime().addShutdownHook(shutdownHook);
  }
}
