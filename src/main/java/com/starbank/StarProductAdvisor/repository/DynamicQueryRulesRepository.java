package com.starbank.StarProductAdvisor.repository;

import com.starbank.StarProductAdvisor.entity.DynamicQueryRules;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicQueryRulesRepository extends CrudRepository<DynamicQueryRules, Long> {
}
