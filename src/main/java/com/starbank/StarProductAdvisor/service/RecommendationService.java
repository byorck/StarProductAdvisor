package com.starbank.StarProductAdvisor.service;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.exception.DatabaseException;
import com.starbank.StarProductAdvisor.exception.UserNotFoundException;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import com.starbank.StarProductAdvisor.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сервис рекомендаций.
 * Хранит список всех правил RecommendationRuleSet и применяет их к пользователю,
 * собирая все подходящие рекомендации.
 */
@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final RecommendationsRepository repository;

    public RecommendationService(List<RecommendationRuleSet> ruleSets, RecommendationsRepository repository) {
        this.ruleSets = ruleSets;
        this.repository = repository;
    }

    /**
     * Получает список рекомендаций для пользователя по его UUID.
     * Выполняет предварительную проверку, существует ли пользователь в базе.
     * Если пользователь не найден, выбрасывается {@link UserNotFoundException}.
     * Перебирает все подключённые правила рекомендаций и собирает подходящие в список.
     *
     * @param userId UUID пользователя, для которого ищутся рекомендации
     * @return список рекомендаций {@link RecommendationDTO}, может быть пустым
     * @throws UserNotFoundException если пользователь с заданным userId не найден
     * @throws DatabaseException     в случае технических неполадок при обращении к базе данных
     */
    public List<RecommendationDTO> getRecommendationsForUser(UUID userId) {
        if (!repository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        List<RecommendationDTO> recommendations = new ArrayList<>();
        for (RecommendationRuleSet rule : ruleSets) {
            rule.apply(userId).ifPresent(recommendations::add);
        }
        return recommendations;
    }
}