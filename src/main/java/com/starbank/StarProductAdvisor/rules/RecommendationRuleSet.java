package com.starbank.StarProductAdvisor.rules;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import java.util.Optional;
import java.util.UUID;
/**
 * Интерфейс правила для проверки, подходит ли конкретная рекомендация пользователю.
 * Метод apply принимает userId и возвращает Optional с рекомендацией, если правило выполнено.
 */
public interface RecommendationRuleSet {
    Optional<RecommendationDTO> apply(UUID userId);
}