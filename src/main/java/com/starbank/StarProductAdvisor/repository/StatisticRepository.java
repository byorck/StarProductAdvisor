package com.starbank.StarProductAdvisor.repository;

import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Optional<Statistic> findByDynamicRecommendationRule(DynamicRecommendationRule dynamicRecommendationRule);
}
