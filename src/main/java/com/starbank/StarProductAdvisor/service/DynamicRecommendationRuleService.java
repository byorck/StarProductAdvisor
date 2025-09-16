package com.starbank.StarProductAdvisor.service;

import com.starbank.StarProductAdvisor.dto.DynamicQueryRulesDTO;
import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleWithIdDTO;
import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleWithoutIdDTO;
import com.starbank.StarProductAdvisor.entity.DynamicQueryRules;
import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.repository.DynamicRecommendationRuleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class DynamicRecommendationRuleService {

    private final DynamicRecommendationRuleRepository dynamicRecommendationRuleRepository;

    public DynamicRecommendationRuleService(DynamicRecommendationRuleRepository dynamicRecommendationRuleRepository) {
        this.dynamicRecommendationRuleRepository = dynamicRecommendationRuleRepository;
    }

    // Создание правила
    public DynamicRecommendationRule createDynamicRecommendationRule(DynamicRecommendationRuleWithoutIdDTO dto) {
        DynamicRecommendationRule entity = mapDtoToEntity(dto);
        if (entity.getQueries() != null) {
            entity.getQueries().forEach(q -> q.setDynamicRecommendationRule(entity));
        }
        return dynamicRecommendationRuleRepository.save(entity);
    }

    // Преобразование DTO -> Entity
    private DynamicRecommendationRule mapDtoToEntity(DynamicRecommendationRuleWithoutIdDTO dto) {
        DynamicRecommendationRule entity = new DynamicRecommendationRule();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setProductText(dto.getProductText());

        Set<DynamicQueryRules> queries = dto.getRule().stream()
                .map(qdto -> {
                    DynamicQueryRules q = new DynamicQueryRules();
                    q.setQuery(qdto.getQuery());
                    q.setArguments(qdto.getArguments());
                    q.setNegate(qdto.isNegate());
                    return q;
                }).collect(Collectors.toSet());

        entity.setQueries(queries);
        return entity;
    }

    // Получение всех правил в формате DTO
    public List<DynamicRecommendationRuleWithIdDTO> getAllDynamicRecommendationRuleDto() {
        return dynamicRecommendationRuleRepository.findAll()
                .stream()
                .map(this::mapWithIdEntityToDto)
                .collect(Collectors.toList());
    }

    // Получение одного правила по ID в формате DTO
    public Optional<DynamicRecommendationRuleWithIdDTO> getDynamicRecommendationRuleDto(Long recommendationId) {
        return dynamicRecommendationRuleRepository.findById(recommendationId)
                .map(this::mapWithIdEntityToDto);
    }

    // Удаление правила с ответом HTTP
    public ResponseEntity<Void> deleteDynamicRecommendationRule(Long recommendationId) {
        Optional<DynamicRecommendationRule> entityOpt = dynamicRecommendationRuleRepository.findById(recommendationId);
        if (entityOpt.isPresent()) {
            dynamicRecommendationRuleRepository.delete(entityOpt.get());
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.notFound().build(); // HTTP 404
    }

    // Маппер c Id Entity -> DTO
    public DynamicRecommendationRuleWithIdDTO mapWithIdEntityToDto(DynamicRecommendationRule entity) {
        DynamicRecommendationRuleWithIdDTO dto = new DynamicRecommendationRuleWithIdDTO();
        dto.setId(entity.getId());  // Заполняем id
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setProductText(entity.getProductText());

        List<DynamicQueryRulesDTO> rulesDto = entity.getQueries().stream()
                .map(query -> {
                    DynamicQueryRulesDTO dtoQuery = new DynamicQueryRulesDTO();
                    dtoQuery.setQuery(query.getQuery());
                    dtoQuery.setArguments(query.getArguments());
                    dtoQuery.setNegate(query.isNegate());
                    return dtoQuery;
                })
                .sorted(Comparator.comparing(DynamicQueryRulesDTO::getQuery))
                .collect(Collectors.toList());

        dto.setRule(rulesDto);
        return dto;
    }
}