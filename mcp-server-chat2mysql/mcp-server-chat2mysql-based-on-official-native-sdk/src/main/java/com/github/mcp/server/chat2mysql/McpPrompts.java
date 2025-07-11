package com.github.mcp.server.chat2mysql;

import com.github.mcp.server.chat2mysql.enums.PromptMessageTemplate;
import com.github.mcp.server.chat2mysql.util.SqlHelper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

import java.text.MessageFormat;
import java.util.HashMap;
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
     * Please see the README.md of this project for more details.
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

            // Format the prompt template.
            Set<String> tableNames = SqlHelper.parseTableNames(sqlStr);
            Map<String, String> tableSchemas = new HashMap<>(tableNames.size());
            for (String tableName : tableNames) {
                final String tableSchema = SqlHelper.showCreateTable(tableName);
                tableSchemas.put(tableName, tableSchema);
            }
            final String promptTemplate = PromptMessageTemplate.getBySystemLanguage();
            final String explain = SqlHelper.explainSql(sqlStr);
            final String formatted = MessageFormat.format(promptTemplate, sqlStr, tableSchemas, explain);

            // Create the prompt message and result.
            McpSchema.TextContent content = new McpSchema.TextContent(formatted);
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
