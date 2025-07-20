# Filesystem MCP Server

Java server implementing Model Context Protocol (MCP) for filesystem operations.

## Features

- Read file from the local filesystem.
- Delete file to the local filesystem.
- Find files or directories from the local filesystem.

## API

### Resources

- `file://system`: File system operations interface.

### Prompts

- **find**
  - Start from the specified starting path and recursively search for sub-files or sub-directories.
  - Input:
    * `start` (string): The starting path to search, required.
    * `name` (string): The name of the target file or directory to search, supports fuzzy matching, required.
  - Output:
    * Call the MCP tool 'find' to search for files or directories whose name matches: 'test.txt', starting from the specified start path: '/home/user/codeboyzhou'

- **read**
    - Read the contents of a file or non-recursively read the sub-files and sub-directories under a directory.
    - Input:
        * `path` (string): The path to read, can be a file or directory, required.
    - Output:
      * Call the MCP tool 'read' to read the file or directory: '/home/user/codeboyzhou'

- **delete**
    - Delete a file or directory from the filesystem.
    - Input:
        * `path` (string): The path to delete, can be a file or directory, required.
    - Output:
      * Call the MCP tool 'delete' to delete the file or directory: '/home/user/codeboyzhou'

### Tools

- **find**
    - Start from the specified starting path and recursively search for sub-files or sub-directories.
    - Input:
        * `start` (string): The starting path to search, required.
        * `name` (string): The name of the target file or directory to search, supports fuzzy matching, required.
    - Output:
      * A list of absolute path strings for all matching entries found during the search.

- **read**
    - Read the contents of a file or non-recursively read the sub-files and sub-directories under a directory.
    - Input:
        * `path` (string): The path to read, can be a file or directory, required.
    - Output:
      * If the path points to a file, it returns a string containing the file's content.
        If the path points to a directory, it returns a list of strings representing the direct children
        (immediate subdirectories and files) directly under the specified directory (non-recursive).

- **delete**
    - Delete a file or directory from the filesystem.
    - Input:
        * `path` (string): The path to delete, can be a file or directory, required.
    - Output:
      * The operation result, for example: `Successfully deleted path: /home/user/codeboyzhou`

## Usage with MCP Client

You can use any MCP client such as Cursor (IDE) or Cline (VS Code plugin) to interact with MCP server.

### Build

```shell
mvn clean package
```

### Configuration

```json
{
  "mcpServers": {
    "mcp-server-filesystem": {
      "command": "java",
      "args": [
        "-jar",
        "${your_jar_file_path}"
      ]
    }
  }
}
```

## Usage with FastAgent (https://github.com/evalstate/fast-agent)

### Build

```shell
mvn clean package
```

### Configuration

```shell
cd agent
```

Rename `fastagent-config.yaml.example` to `fastagent-config.yaml` and configure the default LLM and MCP server.

Rename `fastagent-secrets.yaml.example` to `fastagent-secrets.yaml` and configure your LLM API key.

```shell
pip install uv
uv sync
uv run agent.py
```
