package com.github.mcp.server.filesystem.common;

import jakarta.servlet.http.HttpServlet;
import java.time.Duration;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple HTTP server implementation based on Jetty. This class provides methods to configure and
 * start an HTTP server that can host a servlet.
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 */
public class HttpServer {
  /** Logger instance for logging server events and errors. */
  private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

  /** Default context path for the servlet. */
  private static final String DEFAULT_SERVLET_CONTEXT_PATH = "/";

  /** Default path mapping for the servlet. */
  private static final String DEFAULT_SERVLET_PATH = "/*";

  /** The servlet to be hosted by this HTTP server. */
  private HttpServlet servlet;

  /** The port on which the HTTP server will listen. */
  private int port;

  /**
   * Sets the servlet to be hosted by this HTTP server.
   *
   * @param servlet the servlet to host
   * @return this HttpServer instance for method chaining
   */
  public HttpServer use(HttpServlet servlet) {
    this.servlet = servlet;
    return this;
  }

  /**
   * Sets the port on which the HTTP server will listen.
   *
   * @param port the port number
   * @return this HttpServer instance for method chaining
   */
  public HttpServer bind(int port) {
    this.port = port;
    return this;
  }

  /**
   * Starts the HTTP server with the configured servlet and port. This method blocks indefinitely
   * until the server is stopped. The server will automatically shut down when the JVM receives a
   * shutdown signal.
   */
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

  /**
   * Adds a shutdown hook to gracefully stop the server when the JVM shuts down.
   *
   * @param httpserver the server to stop
   */
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
