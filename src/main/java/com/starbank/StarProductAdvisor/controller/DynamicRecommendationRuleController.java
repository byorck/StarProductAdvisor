package com.starbank.StarProductAdvisor.controller;

import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleWithIdDTO;
import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleWithoutIdDTO;
import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.service.DynamicRecommendationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Управление банковскими продуктами с динамическими правилами", description = "Содержит операции по управлению " +
        "банковскими продуктами с динамическими правилами: добавление, удаление, получение правила, получение списка всех правил")
@RestController
@RequestMapping("/rule")
public class DynamicRecommendationRuleController {
    private final DynamicRecommendationRuleService dynamicRecommendationRuleService;

    public DynamicRecommendationRuleController(DynamicRecommendationRuleService dynamicRecommendationRuleService) {
        this.dynamicRecommendationRuleService = dynamicRecommendationRuleService;
    }

    @PostMapping
    @Operation(summary = "Создание банковского продукта", description = "В ответе возвращается информация о добавленном банковском " +
            "продукте, со следующими полями: id, product_name, product_id, product_text и rules.")
    public DynamicRecommendationRuleWithIdDTO createDynamicRecommendationRule(@RequestBody DynamicRecommendationRuleWithoutIdDTO dto) {
        DynamicRecommendationRule entity = dynamicRecommendationRuleService.createDynamicRecommendationRule(dto);
        return dynamicRecommendationRuleService.mapWithIdEntityToDto(entity);
    }

    @GetMapping("/{recommendationId}")
    @Operation(summary = "Получение банковского продукта по его ID", description = "В ответе возвращается информация о банковском " +
            "продукте, со следующими полями: id, product_name, product_id, product_text и rules.")
    public Optional<DynamicRecommendationRuleWithIdDTO> getDynamicRecommendationRule(@PathVariable Long recommendationId) {
        return dynamicRecommendationRuleService.getDynamicRecommendationRuleDto(recommendationId);
    }

    @GetMapping
    @Operation(summary = "Получение всех банковских продуктов", description = "В ответе возвращается список со всеми банковскими " +
            "продуктами, со следующими полями: id, product_name, product_id, product_text и rules.")
    public Map<String, Object> getAllDynamicRecommendationRule() {
        List<DynamicRecommendationRuleWithIdDTO> rules = dynamicRecommendationRuleService.getAllDynamicRecommendationRuleDto();
        return Collections.singletonMap("data", rules);
    }

    @DeleteMapping("/{recommendationId}")
    @Operation(summary = "Удаление динамического правила по его ID")
    public ResponseEntity<Void> deleteDynamicRecommendationRule(@PathVariable Long recommendationId) {
        return dynamicRecommendationRuleService.deleteDynamicRecommendationRule(recommendationId);
    }
}
