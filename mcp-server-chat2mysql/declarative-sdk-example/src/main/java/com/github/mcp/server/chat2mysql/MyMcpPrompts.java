package com.github.mcp.server.chat2mysql;

import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompt;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPromptParam;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompts;
import com.github.mcp.server.chat2mysql.enums.PromptMessageEnding;
import com.github.mcp.server.chat2mysql.util.SqlHelper;

import java.util.Set;

/**
 * A class that defines MCP prompts to the MCP server.
 *
 * @author codeboyzhou
 */
@McpPrompts
public class MyMcpPrompts {

    /**
     * Create a MCP prompt to generate SQL optimization tips.
     *
     * @param sql The SQL query to optimize.
     * @return The prompt message, please see the README.md of this project for more details.
     */
    @McpPrompt(name = "generate_sql_optimization_tips", description = "Generate SQL optimization tips.")
    public static String generateSqlOptimizationTips(
        @McpPromptParam(name = "sql", description = "The SQL query to optimize.", required = true) String sql
    ) {
        StringBuilder promptTemplate = new StringBuilder("""
            There is an SQL statement along with its EXPLAIN plan and table schemas.
            Please analyze the query performance and provide optimization recommendations.""")
            .append("\n\n")
            .append("The SQL statement is: ").append(sql)
            .append("\n\n");

        Set<String> tableNames = SqlHelper.parseTableNames(sql);
        for (String tableName : tableNames) {
            final String tableSchema = SqlHelper.showCreateTable(tableName);
            promptTemplate.append("The table schema for ").append(tableName).append(" is: ").append(tableSchema)
                .append("\n\n");
        }

        promptTemplate.append("The EXPLAIN plan for the SQL statement is: ").append(SqlHelper.explainSql(sql));
        promptTemplate.append("\n\nPlease provide optimization recommendations for the SQL statement.");
        promptTemplate.append("\n\n").append(PromptMessageEnding.ofCurrentUserLanguage());

        return promptTemplate.toString();
    }

}
