import asyncio

from mcp_agent.core.fastagent import FastAgent

# Create the application
fast = FastAgent("FastAgent for MCP Server Chat2MySQL")


# Define the agent
@fast.agent(
    name="mcp-server-chat2mysql",
    instruction="""You are a helpful AI Agent that is able to interact with users
    and to call the resources, prompts, tools of the MCP server named chat2mysql.
    """,
    servers=["chat2mysql"]
)
async def main():
    # use the --model command line switch or agent arguments to change model
    async with fast.run() as agent:
        await agent.interactive()


if __name__ == "__main__":
    asyncio.run(main())
