package com.starbank.StarProductAdvisor.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "statistics")
@Schema(description = "статистики срабатывания правил рекомендаций.")
public class Statistic {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recommendation_id")
    private DynamicRecommendationRule dynamicRecommendationRule;  //id правила

    @Schema(description = "Счетчик")
    private Long count = 0L; // число срабатываний этого правила

    public Statistic(Long id, DynamicRecommendationRule dynamicRecommendationRule, Long count) {
        this.id = id;
        this.dynamicRecommendationRule = dynamicRecommendationRule;
        this.count = count;
    }

    public Statistic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DynamicRecommendationRule getDynamicRecommendationRule() {
        return dynamicRecommendationRule;
    }

    public void setDynamicRecommendationRule(DynamicRecommendationRule dynamicRecommendationRule) {
        this.dynamicRecommendationRule = dynamicRecommendationRule;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
