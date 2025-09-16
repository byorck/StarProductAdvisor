package com.starbank.StarProductAdvisor.repository;

import com.starbank.StarProductAdvisor.entity.DynamicQueryRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicQueryRulesRepository extends JpaRepository<DynamicQueryRules, Long> {
}
