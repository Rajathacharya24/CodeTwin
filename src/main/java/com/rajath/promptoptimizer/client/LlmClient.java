package com.rajath.promptoptimizer.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.List;

@Component
public class LlmClient {

    private final WebClient llmWebClient;

    public LlmClient(WebClient llmWebClient) {
        this.llmWebClient = llmWebClient;
    }

    @SuppressWarnings("unchecked")
    public String getChatCompletion(String systemPrompt, String userPrompt) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.3
        );

        Map response = llmWebClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response != null && response.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
        }
        throw new RuntimeException("Invalid response from LLM API");
    }
}
