package com.starbank.StarProductAdvisor.repository;

import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicRecommendationRuleRepository extends JpaRepository<DynamicRecommendationRule, Long> {
}
