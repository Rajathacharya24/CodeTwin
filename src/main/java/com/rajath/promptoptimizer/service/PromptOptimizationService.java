package com.rajath.promptoptimizer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajath.promptoptimizer.client.LlmClient;
import com.rajath.promptoptimizer.dto.OptimizeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class PromptOptimizationService {

    private static final Logger log = LoggerFactory.getLogger(PromptOptimizationService.class);

    private static final String META_PROMPT = 
            "You are a prompt engineering expert. Given the user's prompt, return ONLY valid JSON, no markdown fences, no extra text:\n" +
            "{\n" +
            "  \"optimizedPrompt\": \"...\",\n" +
            "  \"suggestions\": [{\"issue\": \"...\", \"fix\": \"...\", \"category\": \"clarity|specificity|structure|context|format\"}],\n" +
            "  \"clarityScore\": 1-10\n" +
            "}\n";

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    public PromptOptimizationService(LlmClient llmClient, ObjectMapper objectMapper) {
        this.llmClient = llmClient;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "prompts", key = "#prompt")
    public OptimizeResponse optimizePrompt(String prompt) {
        log.info("Optimizing prompt: {}", prompt);
        String responseContent = llmClient.getChatCompletion(META_PROMPT, "User's prompt: \"" + prompt + "\"");
        
        try {
            return parseResponse(responseContent);
        } catch (Exception e) {
            log.warn("Failed to parse LLM response, attempting retry: {}", e.getMessage());
            // Retry logic
            String retryPrompt = "Return valid JSON only. User's prompt: \"" + prompt + "\"";
            responseContent = llmClient.getChatCompletion(META_PROMPT, retryPrompt);
            try {
                return parseResponse(responseContent);
            } catch (Exception ex) {
                log.error("Failed to parse LLM response on retry", ex);
                throw new RuntimeException("Failed to optimize prompt. LLM did not return valid JSON.");
            }
        }
    }

    private OptimizeResponse parseResponse(String content) throws JsonProcessingException {
        // Strip markdown fences if LLM ignored instructions
        if (content.startsWith("```json")) {
            content = content.substring(7);
        } else if (content.startsWith("```")) {
            content = content.substring(3);
        }
        if (content.endsWith("```")) {
            content = content.substring(0, content.length() - 3);
        }
        return objectMapper.readValue(content.trim(), OptimizeResponse.class);
    }
}
