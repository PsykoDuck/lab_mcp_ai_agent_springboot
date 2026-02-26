package com.example.agent;

import com.example.agent.agent.BacklogAgent;
import dev.langchain4j.model.chat.ChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ActiveProfiles("test")
class AgentApplicationTests {

	@MockBean
	ChatModel chatModel;

	@MockBean
	BacklogAgent backlogAgent;

	@Test
	void contextLoads() {
	}

}
