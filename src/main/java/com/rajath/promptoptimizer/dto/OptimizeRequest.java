package com.rajath.promptoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OptimizeRequest {

    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
