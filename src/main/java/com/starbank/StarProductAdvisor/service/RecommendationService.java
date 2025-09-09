package com.starbank.StarProductAdvisor.service;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
/**
 * Сервис рекомендаций.
 * Хранит список всех правил RecommendationRuleSet и применяет их к пользователю,
 * собирая все подходящие рекомендации.
 */
@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    /**
     * Получает список рекомендаций для пользователя по его UUID,
     * перебирая все правила и добавляя подходящие рекомендации в список.
     */
    public List<RecommendationDTO> getRecommendationsForUser(UUID userId) {
        List<RecommendationDTO> recommendations = new ArrayList<>();
        for (RecommendationRuleSet rule : ruleSets) {
            rule.apply(userId).ifPresent(recommendations::add);
        }
        return recommendations;
    }
}