# Chat2MySQL MCP Server

Java server implementing Model Context Protocol (MCP) for chat2mysql operations.

Use AI agent that supports multi-language to chat with your MySQL database.

## Features

- Generate SQL optimization tips.

## API

### Prompts

- **generate_sql_optimization_tips**
  - Generate SQL optimization tips.
  - Input:
    * `sql` (string): The SQL query to optimize, required.
  - Output: A prompt message like below.
  ```sql
  There is an SQL statement along with its EXPLAIN plan and table schemas.
  Please analyze the query performance and provide optimization recommendations.

  The SQL statement is: SELECT * FROM `test` WHERE id = 1;

  The table schema for `test` is: < actual table schema of `test` >

  The EXPLAIN plan for the SQL statement is: < actual EXPLAIN plan of the query >

  Please provide optimization recommendations for the SQL statement.

  Please answer in English (Note: This ending sentence depends on what language your OS is currently using)
  ```

## Usage with MCP Client

You can use any MCP client such as Cursor (IDE) or Cline (VS Code plugin) to interact with MCP server.

### Build

```bash
mvn clean package
```

### Configuration

```json
{
  "mcpServers": {
    "mcp-server-chat2mysql": {
      "command": "java",
      "args": [
        "-jar",
        "${your_jar_path}/mcp-server-chat2mysql.jar"
      ],
      "env": {
        "MYSQL_HOST": "localhost",
        "MYSQL_PORT": "3306",
        "MYSQL_USER": "root",
        "MYSQL_PASSWORD": "your_mysql_password",
        "MYSQL_DB_NAME": "test"
      }
    }
  }
}
```

## Usage with FastAgent

### Build

```bash
mvn clean package
```

### Configuration

Rename `fastagent-config.yaml.example` to `fastagent-config.yaml` and configure the default LLM you want to use.

Rename `fastagent-secrets.yaml.example` to `fastagent-secrets.yaml` and configure your LLM API key.

```bash
pip install uv
uv pip install fast-agent-mcp
uv run agent.py
```
