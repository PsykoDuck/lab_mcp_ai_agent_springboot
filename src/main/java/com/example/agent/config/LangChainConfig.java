package com.example.agent.config;

import com.example.agent.agent.BacklogAgent;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class LangChainConfig {

    @Bean
    public AnthropicChatModel anthropicChatModel(
            @Value("${anthropic.api-key}") String apiKey,
            @Value("${anthropic.model}") String model,
            @Value("${anthropic.timeout-seconds:60}") Integer timeoutSeconds
    ) {
        return AnthropicChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }

    /**
     * ObjectProvider allows Spring to inject all AgentTool beans if they exist (STEP 6+),
     * or an empty list if none are registered yet.
     */
    @Bean
    public BacklogAgent backlogAgent(AnthropicChatModel model, ObjectProvider<Object> toolsProvider) {
        List<Object> tools = toolsProvider.stream().toList();

        System.out.println("=== Agent tools loaded: " + tools.size() + " ===");
        tools.forEach(t -> System.out.println(" - " + t.getClass().getName()));

        return AiServices.builder(BacklogAgent.class)
                .chatModel(model)
                .tools(tools.toArray())
                .build();
    }
}
