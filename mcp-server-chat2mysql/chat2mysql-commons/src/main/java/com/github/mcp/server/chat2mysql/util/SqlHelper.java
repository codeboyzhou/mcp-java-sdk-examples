package com.github.mcp.server.chat2mysql.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public final class SqlHelper {

    private static final Logger logger = LoggerFactory.getLogger(SqlHelper.class);

    private static final String WHITESPACE = "\\s+";

    private static final String JDBC_URL = String.format(
        "jdbc:mysql://%s:%s/%s?useSSL=false&characterEncoding=UTF-8",
        System.getenv("MYSQL_HOST"),
        System.getenv("MYSQL_PORT"),
        System.getenv("MYSQL_DB_NAME")
    );

    private static final String MYSQL_USER = System.getenv("MYSQL_USER");

    private static final String MYSQL_PASSWORD = System.getenv("MYSQL_PASSWORD");

    public static Set<String> parseTableNames(String sql) {
        logger.debug("Parsing table names from SQL statement: {}", sql);
        Set<String> tableNames = new HashSet<>();
        String[] chunks = sql.split(WHITESPACE);
        for (int i = 0; i < chunks.length; i++) {
            final String chunk = chunks[i];
            if (chunk.equalsIgnoreCase("FROM") || chunk.equalsIgnoreCase("JOIN")) {
                final String tableName = chunks[i + 1];
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }

    public static String showCreateTable(String tableName) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, MYSQL_USER, MYSQL_PASSWORD)) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SHOW CREATE TABLE " + tableName);
                String result = null;
                while (resultSet.next()) {
                    result = resultSet.getString("Create Table");
                }
                return result;
            }
        } catch (SQLException e) {
            logger.error("Error connecting to MySQL database with jdbc url {}", JDBC_URL, e);
            return formatExceptionStackTrace(e);
        }
    }

    public static String explainSql(String sql) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, MYSQL_USER, MYSQL_PASSWORD)) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("EXPLAIN " + sql);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                final int columnCount = resultSetMetaData.getColumnCount();
                StringBuilder result = new StringBuilder("\n");
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        final String columnLabel = resultSetMetaData.getColumnLabel(i);
                        final String columnValue = resultSet.getString(columnLabel);
                        result.append(columnLabel).append(" = ").append(columnValue).append(" | ");
                    }
                    result.append("\n");
                }
                return result.toString();
            }
        } catch (SQLException e) {
            logger.error("Error connecting to MySQL database with jdbc url {}", JDBC_URL, e);
            return formatExceptionStackTrace(e);
        }
    }

    private static String formatExceptionStackTrace(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StringBuilder result = new StringBuilder(stackTrace[0] + ": " + throwable.getMessage());
        for (int i = 1; i < stackTrace.length; i++) {
            result.append("\n\tat ").append(stackTrace[i]);
        }
        return result.toString();
    }

}
