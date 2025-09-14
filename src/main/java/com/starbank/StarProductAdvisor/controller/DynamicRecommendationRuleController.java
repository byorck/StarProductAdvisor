package com.starbank.StarProductAdvisor.controller;

import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleDTO;
import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.service.DynamicRecommendationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rule")
public class DynamicRecommendationRuleController {
    private final DynamicRecommendationRuleService dynamicRecommendationRuleService;

    public DynamicRecommendationRuleController(DynamicRecommendationRuleService dynamicRecommendationRuleService) {
        this.dynamicRecommendationRuleService = dynamicRecommendationRuleService;
    }

    @PostMapping
    @Operation(summary = "Создание динамического правила")
    public DynamicRecommendationRule createDynamicRecommendationRule(@RequestBody DynamicRecommendationRuleDTO dynamicRecommendationRule) {
        return dynamicRecommendationRuleService.createDynamicRecommendationRule(dynamicRecommendationRule);
    }
}
