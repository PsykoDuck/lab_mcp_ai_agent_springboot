package com.example.agent.web;

import com.example.agent.agent.BacklogAgent;
import dev.langchain4j.model.chat.ChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AgentControllerIT {

    @MockBean
    ChatModel chatModel;

    @MockBean
    BacklogAgent backlogAgent;

    @Autowired
    WebTestClient web;

    @Test
    void should_return_agent_response_on_post_run() {
        when(backlogAgent.handle(anyString()))
                .thenReturn("Issue created successfully.");

        String response = web.post()
                .uri("/api/run")
                .bodyValue("Create a task to add OpenTelemetry")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotBlank();
        assertThat(response).contains("Issue created successfully.");
    }

    @Test
    void should_delegate_prompt_to_backlog_agent() {
        when(backlogAgent.handle("my prompt")).thenReturn("agent response");

        web.post()
                .uri("/api/run")
                .bodyValue("my prompt")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("agent response");
    }
}
