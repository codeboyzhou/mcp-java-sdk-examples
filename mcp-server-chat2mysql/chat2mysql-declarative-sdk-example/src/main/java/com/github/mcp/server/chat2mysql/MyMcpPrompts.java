package com.github.mcp.server.chat2mysql;

import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompt;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPromptParam;
import com.github.codeboyzhou.mcp.declarative.annotation.McpPrompts;
import com.github.mcp.server.chat2mysql.enums.PromptMessageTemplate;
import com.github.mcp.server.chat2mysql.util.SqlHelper;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
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
    @McpPrompt(name = "generate_sql_optimization_tips", descriptionI18nKey = "generate_sql_optimization_tips_prompt_description")
    public static String generateSqlOptimizationTips(
        @McpPromptParam(name = "sql", descriptionI18nKey = "generate_sql_optimization_tips_prompt_param_sql_description", required = true) String sql
    ) {
        Set<String> tableNames = SqlHelper.parseTableNames(sql);
        Map<String, String> tableSchemas = new HashMap<>(tableNames.size());
        for (String tableName : tableNames) {
            final String tableSchema = SqlHelper.showCreateTable(tableName);
            tableSchemas.put(tableName, tableSchema);
        }
        final String promptTemplate = PromptMessageTemplate.getBySystemLanguage();
        final String explain = SqlHelper.explainSql(sql);
        return MessageFormat.format(promptTemplate, sql, tableSchemas, explain);
    }

}
