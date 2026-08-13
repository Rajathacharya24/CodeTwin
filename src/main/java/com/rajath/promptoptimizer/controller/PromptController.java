package com.rajath.promptoptimizer.controller;

import com.rajath.promptoptimizer.dto.OptimizeRequest;
import com.rajath.promptoptimizer.dto.OptimizeResponse;
import com.rajath.promptoptimizer.service.PromptOptimizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompts")
@CrossOrigin(origins = "*") // Allows local frontend testing
public class PromptController {

    private final PromptOptimizationService optimizationService;

    public PromptController(PromptOptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizeResponse> optimizePrompt(@Valid @RequestBody OptimizeRequest request) {
        OptimizeResponse response = optimizationService.optimizePrompt(request.getPrompt());
        return ResponseEntity.ok(response);
    }
}
