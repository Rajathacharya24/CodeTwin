package com.rajath.promptoptimizer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajath.promptoptimizer.dto.OptimizeRequest;
import com.rajath.promptoptimizer.dto.OptimizeResponse;
import com.rajath.promptoptimizer.service.PromptOptimizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromptController.class)
class PromptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromptOptimizationService optimizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void optimizePrompt_Success() throws Exception {
        OptimizeRequest request = new OptimizeRequest();
        request.setPrompt("Test prompt");

        OptimizeResponse response = new OptimizeResponse();
        response.setOptimizedPrompt("Optimized prompt");
        response.setClarityScore(8);

        when(optimizationService.optimizePrompt(anyString())).thenReturn(response);

        mockMvc.perform(post("/api/prompts/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.optimizedPrompt").value("Optimized prompt"))
                .andExpect(jsonPath("$.clarityScore").value(8));
    }

    @Test
    void optimizePrompt_ValidationFailure() throws Exception {
        OptimizeRequest request = new OptimizeRequest();
        request.setPrompt(""); // Blank prompt

        mockMvc.perform(post("/api/prompts/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.prompt").value("Prompt cannot be empty"));
    }
}
