package com.example.agent.tools;

import com.example.agent.mcp.McpHttpClient;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GitHubMcpToolsTest {

    @Test
    void should_call_mcp_tool_and_return_success_message() {
        McpHttpClient mcp = mock(McpHttpClient.class);
        when(mcp.callTool(eq("create_issue"), anyMap()))
                .thenReturn(Mono.just(Map.of("number", 42, "html_url", "https://github.com/owner/repo/issues/42")));

        GitHubMcpTools tools = new GitHubMcpTools(mcp, "owner", "repo");
        String result = tools.createIssue("Test title", "Test body");

        assertTrue(result.contains("Issue created successfully"));
        verify(mcp, times(1)).callTool(eq("create_issue"), anyMap());
    }

    @Test
    void should_pass_owner_repo_title_body_to_mcp() {
        McpHttpClient mcp = mock(McpHttpClient.class);
        when(mcp.callTool(anyString(), anyMap()))
                .thenReturn(Mono.just(Map.of("number", 1)));

        GitHubMcpTools tools = new GitHubMcpTools(mcp, "my-owner", "my-repo");
        tools.createIssue("My title", "My body");

        verify(mcp).callTool(eq("create_issue"), argThat(args ->
                "my-owner".equals(args.get("owner")) &&
                "my-repo".equals(args.get("repo")) &&
                "My title".equals(args.get("title")) &&
                "My body".equals(args.get("body"))
        ));
    }
}
