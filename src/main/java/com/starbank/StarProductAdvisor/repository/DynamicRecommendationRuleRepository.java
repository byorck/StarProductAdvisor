package com.starbank.StarProductAdvisor.repository;

import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicRecommendationRuleRepository extends CrudRepository<DynamicRecommendationRule, Long> {
}
