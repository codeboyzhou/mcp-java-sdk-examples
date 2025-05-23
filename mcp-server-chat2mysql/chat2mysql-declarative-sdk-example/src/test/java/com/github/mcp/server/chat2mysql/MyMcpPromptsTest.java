package com.github.mcp.server.chat2mysql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.github.mcp.server.chat2mysql.util.SqlHelper;
import java.util.Set;

class MyMcpPromptsTest {

    @Test
    void generateSqlOptimizationTips_includesSqlAndPromptHeader() {
        String sql = "SELECT * FROM users WHERE id = 1;";
        try (MockedStatic<SqlHelper> helper = Mockito.mockStatic(SqlHelper.class)) {
            helper.when(() -> SqlHelper.parseTableNames(sql))
                  .thenReturn(Set.of("users"));
            helper.when(() -> SqlHelper.showCreateTable("users"))
                  .thenReturn("CREATE TABLE users (id INT PRIMARY KEY);");
            helper.when(() -> SqlHelper.explainSql(sql))
                  .thenReturn("id | select_type | table | type | possible_keys | key | rows | Extra\n1 | SIMPLE | users | ALL | NULL | NULL | 1 | ");

            String prompt = MyMcpPrompts.generateSqlOptimizationTips(sql);

            // verify the prompt is not null or empty
            assertNotNull(prompt, "Prompt should not be null");
            assertFalse(prompt.isBlank(), "Prompt should not be blank");

            // verify it includes the SQL statement
            assertTrue(prompt.contains("The SQL statement is: " + sql),
                "Prompt should include the SQL statement");

            // verify it includes the explain plan section label and content
            assertTrue(prompt.contains("The EXPLAIN plan for the SQL statement is:"),
                "Prompt should include the EXPLAIN plan section");
            assertTrue(prompt.contains("id | select_type"),
                "Prompt should include the mocked EXPLAIN plan content");

            // verify it includes the mocked table schema
            assertTrue(prompt.contains("The table schema for users is: CREATE TABLE users"),
                "Prompt should include the mocked table schema content");

            // verify it ends with the language-specific message ending
            String ending = com.github.mcp.server.chat2mysql.enums.PromptMessageEnding.ofCurrentUserLanguage();
            assertTrue(prompt.endsWith(ending),
                "Prompt should end with the language-specific message ending");
        }
    }
}
