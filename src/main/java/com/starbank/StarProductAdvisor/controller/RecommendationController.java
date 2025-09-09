package com.starbank.StarProductAdvisor.controller;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.dto.RecommendationResponse;
import com.starbank.StarProductAdvisor.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId) {
        List<RecommendationDTO> recs = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(new RecommendationResponse(userId.toString(), recs));
    }
}