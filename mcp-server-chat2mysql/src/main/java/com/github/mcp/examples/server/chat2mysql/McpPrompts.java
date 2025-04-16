package com.github.mcp.examples.server.chat2mysql;

import com.github.mcp.examples.server.chat2mysql.util.SqlHelper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class that defines MCP prompts to the MCP server.
 *
 * @author codeboyzhou
 */
public class McpPrompts {

    /**
     * Create a MCP prompt to generate SQL optimization tips.
     * ------------------------------------------------------
     * Prompt Arguments:
     * sql (string): The SQL query to optimize, required.
     * ------------------------------------------------------
     * Prompt Return:
     * """
     * There is an SQL statement along with its EXPLAIN plan and table schemas.                                                                                                                                                                │
     * Please analyze the query performance and provide optimization recommendations.                                                                                                                                                          │
     * │
     * The SQL statement is: SELECT * FROM `test`                                                                                                                                                                                  │
     * │
     * The table schema for `test` is: CREATE TABLE `test` (
     * // Omit details here
     * ) ENGINE=InnoDB AUTO_INCREMENT=12345 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci                                                                                                                                                 │
     * │
     * The EXPLAIN plan for the SQL statement is:                                                                                                                                                                                              │
     * id = 1 | select_type = SIMPLE | table = payment | partitions = null | type = ALL | possible_keys = null | key = null | key_len = null | ref = null | rows = 16500 | filtered = 100.0 | Extra = null |                                   │
     * |
     * Please provide optimization recommendations for the SQL statement.
     * """
     *
     * @return {@link McpServerFeatures.SyncPromptSpecification}
     */
    public static McpServerFeatures.SyncPromptSpecification generateSqlOptimizationTips() {
        // Step 1: Create a prompt argument with name, description, and required flag.
        McpSchema.PromptArgument sql = new McpSchema.PromptArgument("sql", "The SQL query to optimize.", true);

        // Step 2: Create a prompt with name, description, and arguments.
        McpSchema.Prompt prompt = new McpSchema.Prompt("generate_sql_optimization_tips", "Generate SQL optimization tips.", List.of(sql));

        // Step 3: Create a prompt specification with the prompt and the prompt handler.
        return new McpServerFeatures.SyncPromptSpecification(prompt, (exchange, request) -> {
            // Step 4: Create a prompt message with role and content.
            Map<String, Object> arguments = request.arguments();
            final String sqlStr = arguments.get("sql").toString();

            StringBuilder promptTemplate = new StringBuilder("""
                There is an SQL statement along with its EXPLAIN plan and table schemas.
                Please analyze the query performance and provide optimization recommendations.""")
                .append("\n\n")
                .append("The SQL statement is: ").append(sqlStr)
                .append("\n\n");

            Set<String> tableNames = SqlHelper.parseTableNames(sqlStr);
            for (String tableName : tableNames) {
                final String tableSchema = SqlHelper.showCreateTable(tableName);
                promptTemplate.append("The table schema for ").append(tableName).append(" is: ").append(tableSchema)
                    .append("\n\n");
            }

            promptTemplate.append("The EXPLAIN plan for the SQL statement is: ").append(SqlHelper.explainSql(sqlStr));
            promptTemplate.append("\n\nPlease provide optimization recommendations for the SQL statement.");

            McpSchema.TextContent content = new McpSchema.TextContent(promptTemplate.toString());
            McpSchema.PromptMessage message = new McpSchema.PromptMessage(McpSchema.Role.USER, content);
            return new McpSchema.GetPromptResult(prompt.description(), List.of(message));
        });
    }

    /**
     * Add all prompts to the MCP server.
     *
     * @param server The MCP server to add prompts to.
     */
    public static void addAllTo(McpSyncServer server) {
        server.addPrompt(generateSqlOptimizationTips());
    }

}
