package com.rajath.promptoptimizer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajath.promptoptimizer.client.LlmClient;
import com.rajath.promptoptimizer.dto.OptimizeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptOptimizationServiceTest {

    @Mock
    private LlmClient llmClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PromptOptimizationService service;

    @BeforeEach
    void setUp() {
        // We will use a real ObjectMapper for realistic tests
        service = new PromptOptimizationService(llmClient, new ObjectMapper());
    }

    @Test
    void optimizePrompt_Success() {
        String mockResponse = "{\n" +
                "  \"optimizedPrompt\": \"Optimized\",\n" +
                "  \"suggestions\": [],\n" +
                "  \"clarityScore\": 9\n" +
                "}";

        when(llmClient.getChatCompletion(anyString(), anyString())).thenReturn(mockResponse);

        OptimizeResponse response = service.optimizePrompt("Test prompt");

        assertNotNull(response);
        assertEquals("Optimized", response.getOptimizedPrompt());
        assertEquals(9, response.getClarityScore());
        verify(llmClient, times(1)).getChatCompletion(anyString(), anyString());
    }

    @Test
    void optimizePrompt_WithMarkdownFences() {
        String mockResponse = "```json\n{\n" +
                "  \"optimizedPrompt\": \"Optimized\",\n" +
                "  \"suggestions\": [],\n" +
                "  \"clarityScore\": 8\n" +
                "}\n```";

        when(llmClient.getChatCompletion(anyString(), anyString())).thenReturn(mockResponse);

        OptimizeResponse response = service.optimizePrompt("Test prompt");

        assertNotNull(response);
        assertEquals("Optimized", response.getOptimizedPrompt());
        assertEquals(8, response.getClarityScore());
        verify(llmClient, times(1)).getChatCompletion(anyString(), anyString());
    }

    @Test
    void optimizePrompt_RetryOnInvalidJson() {
        String invalidResponse = "This is not JSON";
        String validResponse = "{\n" +
                "  \"optimizedPrompt\": \"Retried\",\n" +
                "  \"suggestions\": [],\n" +
                "  \"clarityScore\": 7\n" +
                "}";

        when(llmClient.getChatCompletion(anyString(), anyString()))
                .thenReturn(invalidResponse)
                .thenReturn(validResponse);

        OptimizeResponse response = service.optimizePrompt("Test prompt");

        assertNotNull(response);
        assertEquals("Retried", response.getOptimizedPrompt());
        assertEquals(7, response.getClarityScore());
        verify(llmClient, times(2)).getChatCompletion(anyString(), anyString());
    }

    @Test
    void optimizePrompt_FailureOnRetry() {
        String invalidResponse = "This is not JSON";

        when(llmClient.getChatCompletion(anyString(), anyString()))
                .thenReturn(invalidResponse)
                .thenReturn(invalidResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.optimizePrompt("Test prompt");
        });

        assertEquals("Failed to optimize prompt. LLM did not return valid JSON.", exception.getMessage());
        verify(llmClient, times(2)).getChatCompletion(anyString(), anyString());
    }
}
