package com.starbank.StarProductAdvisor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "queries")
@Schema(description = "Запрос динамического правила.")
public class DynamicQueryRules {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Тип запроса", example = "USER_OF, ACTIVE_USER_OF, TRANSACTION_SUM_COMPARE, TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
    @Enumerated(EnumType.STRING)
    @Column(name = "query", columnDefinition = "TEXT")
    private Query query;

    @Schema(description = "Аргументы запроса (тип продукта, тип транзакции, тип сравнения и число, с которым выполняется сравнение)")
    @ElementCollection(fetch = FetchType.EAGER) // EAGER здесь допустим для ElementCollection
    @CollectionTable(name = "query_arguments", joinColumns = @JoinColumn(name = "query_id", referencedColumnName = "id"))
    @Column(name = "argument")
    private List<String> arguments;

    @Schema(description = "Модификатор отрицания", example = "true/false")
    @Column(name = "negate")
    private boolean negate;

    @ManyToOne(fetch = FetchType.LAZY) // Всегда LAZY для связей!
    @JsonIgnore
    @JoinColumn(name = "recommendations_id")
    private DynamicRecommendationRule dynamicRecommendationRule;

    public DynamicQueryRules(Query query, List<String> arguments, boolean negate, DynamicRecommendationRule dynamicRecommendationRule) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
        this.dynamicRecommendationRule = dynamicRecommendationRule;
    }

    public DynamicQueryRules() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public DynamicRecommendationRule getDynamicRecommendationRule() {
        return dynamicRecommendationRule;
    }

    public void setDynamicRecommendationRule(DynamicRecommendationRule dynamicRecommendationRule) {
        this.dynamicRecommendationRule = dynamicRecommendationRule;
    }


}
