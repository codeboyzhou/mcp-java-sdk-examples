# Filesystem MCP Server

Java server implementing Model Context Protocol (MCP) for filesystem operations.

## Features

- Read file from the local filesystem.
- List files of a directory from the local filesystem.

**Note**: The server will only allow operations within directories specified via `env`.

## API

### Resources

- `file://system`: File system operations interface.

### Tools

- **read_file**
  - Read complete file contents with UTF-8 encoding.
  - Input:
    * `path` (string): The filepath to read, required.
  - Output: Complete file contents with UTF-8 encoding.

- **list_files**
  - List files of a directory.
  - Input:
    * `path` (string): The directory path to read, required.
    * `pattern` (string): Regular expression to filter files, optional, default is empty, means no filter.
    * `recursive` (boolean): Whether to list files recursively, optional, default is `false`.
  - Output: A list of file names (paths if 'recursive' is `true`), or empty string if no files found.

## Usage with MCP Client

You can use any MCP client such as Cursor (IDE) or Cline (VS Code plugin) to interact with MCP server.

### Build

```bash
mvn clean package
```

### Configuration

**Note**: The `env` configuration is optional for access control.

`readable`: Read access control with regular expressions, format example: `["^/var/log/(app|sys)\.log$"]`.

`writable`: Write access control with regular expressions, format example: `["^/var/log/(app|sys)\.log$"]`.

```json
{
  "mcpServers": {
    "mcp-server-filesystem": {
      "command": "java",
      "args": [
        "-jar",
        "${your_jar_path}/mcp-server-filesystem.jar"
      ],
      "env": {
        "permission": {
          "readable": [],
          "writable": []
        }
      }
    }
  }
}
```
