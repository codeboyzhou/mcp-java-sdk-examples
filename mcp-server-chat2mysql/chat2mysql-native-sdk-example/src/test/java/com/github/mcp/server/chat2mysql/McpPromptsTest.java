package com.github.mcp.server.chat2mysql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

class McpPromptsTest {

    static {
        // Enable Byte Buddy experimental support for Java 21
        System.setProperty("net.bytebuddy.experimental", "true");
    }

    private static SyncPromptSpecification spec;

    @BeforeAll
    static void setup() {
        // obtain the prompt specification
        spec = McpPrompts.generateSqlOptimizationTips();
    }

    @Test
    void specHasCorrectNameAndDescription() {
        McpSchema.Prompt prompt = spec.prompt();
        assertEquals("generate_sql_optimization_tips", prompt.name(),
            "Prompt name should match");
        assertEquals("Generate SQL optimization tips.", prompt.description(),
            "Prompt description should match");
    }

    @Test
    void specHasSqlArgument() {
        List<McpSchema.PromptArgument> args = spec.prompt().arguments();
        assertEquals(1, args.size(), "There should be exactly one argument");
        McpSchema.PromptArgument arg = args.get(0);
        assertEquals("sql", arg.name(), "Argument name should be 'sql'");
        assertTrue(arg.required(), "SQL argument should be required");
    }

    @Test
    void addAllToRegistersPromptOnServer() {
        McpSyncServer server = mock(McpSyncServer.class);
        McpPrompts.addAllTo(server);
        // verify that addPrompt was called with the correct prompt specification properties
        ArgumentCaptor<SyncPromptSpecification> captor = ArgumentCaptor.forClass(SyncPromptSpecification.class);
        verify(server).addPrompt(captor.capture());
        SyncPromptSpecification actual = captor.getValue();
        assertEquals(spec.prompt().name(),        actual.prompt().name(),        "Prompt name should match");
        assertEquals(spec.prompt().description(), actual.prompt().description(), "Prompt description should match");
        assertEquals(spec.prompt().arguments().size(),
                     actual.prompt().arguments().size(),                     "Argument count should match");
    }
}
