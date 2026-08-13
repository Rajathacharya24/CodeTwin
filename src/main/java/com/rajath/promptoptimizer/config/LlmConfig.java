package com.rajath.promptoptimizer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LlmConfig {

    @Value("${llm.api.key}")
    private String apiKey;

    @Value("${llm.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Bean
    public WebClient llmWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
