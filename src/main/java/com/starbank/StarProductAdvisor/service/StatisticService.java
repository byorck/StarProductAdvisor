package com.starbank.StarProductAdvisor.service;

import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.entity.Statistic;
import com.starbank.StarProductAdvisor.repository.StatisticRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {
    private final StatisticRepository statisticRepository;

    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    // Увеличение счётчика срабатываний рекомендации
    @Transactional
    public void incrementRecommendationCount(DynamicRecommendationRule rule) {
        Statistic statistic = statisticRepository.findByDynamicRecommendationRule(rule)
                .orElseGet(() -> {
                    Statistic newStat = new Statistic();
                    newStat.setDynamicRecommendationRule(rule);
                    newStat.setCount(0L);
                    return newStat;
                });
        statistic.setCount(statistic.getCount() + 1);
        statisticRepository.save(statistic);
    }
}
