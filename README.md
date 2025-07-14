# MCP Server Examples

A collection of MCP examples developed by various SDKs. As of now, the following SDKs are supported.

- [Spring AI MCP SDK](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html)
- [Official MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk)
- [Annotation-driven MCP Java SDK](https://github.com/codeboyzhou/mcp-declarative-java-sdk) (lightweight and easier to use)

## Requirements

- Java 17 or later (Restricted by MCP Java SDK)

## What is MCP?

The [Model Context Protocol (MCP)](https://modelcontextprotocol.io) lets you build servers that expose data and functionality to LLM applications in a secure, standardized way. Think of it like a web API, but specifically designed for LLM interactions. MCP servers can:

- Expose data through **Resources** (think of these sort of like GET endpoints; they are used to load information into the LLM's context)
- Provide functionality through **Tools** (sort of like POST endpoints; they are used to execute code or otherwise produce a side effect)
- Define interaction patterns through **Prompts** (reusable templates for LLM interactions)
- And more!

You can start exploring everything about **MCP** from [here](https://modelcontextprotocol.io).

## Examples

If you are looking for servers implemented with Typescript MCP SDK or Python MCP SDK, see [here](https://github.com/modelcontextprotocol/servers).

These servers aim to demonstrate MCP features and the MCP Java SDK.

- [Chat2MySQL](https://github.com/codeboyzhou/mcp-java-sdk-examples/blob/main/mcp-server-chat2mysql/README.md) - Use AI agent that supports multi-language to chat with your MySQL database
- [Filesystem](https://github.com/codeboyzhou/mcp-java-sdk-examples/blob/main/mcp-server-filesystem/README.md) - Secure file operations with configurable access controls
