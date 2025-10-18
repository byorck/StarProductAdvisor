package com.starbank.StarProductAdvisor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "recommendations")
@Schema(description = "Банковский продукт с динамическими правилами")
public class DynamicRecommendationRule {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Название продукта")
    @Column(name = "product_name")
    private String productName;

    @Schema(description = "ID продукта", example = "147f6a0f-3b91-413b-ab99-87f081d60d5a")
    @Column(name = "product_id")
    private UUID productId;

    @Schema(description = "Описание продукта")
    @Column(name = "product_text")
    private String productText;

    // ИЗМЕНЕНИЕ: FetchType.LAZY вместо EAGER
    @OneToMany(mappedBy = "dynamicRecommendationRule",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY) // ВАЖНО: LAZY вместо EAGER
    private Set<DynamicQueryRules> queries = new HashSet<>();

    public DynamicRecommendationRule(Long id, UUID productId, String productName, String productText, Set<DynamicQueryRules> queries) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
        this.queries = queries;

        for (DynamicQueryRules q : queries) {
            q.setDynamicRecommendationRule(this);
        }
    }

    public DynamicRecommendationRule() {
    }

    // Вспомогательные методы для управления связью
    public void addQuery(DynamicQueryRules query) {
        this.queries.add(query);
        query.setDynamicRecommendationRule(this);
    }

    public void removeQuery(DynamicQueryRules query) {
        this.queries.remove(query);
        query.setDynamicRecommendationRule(null);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public Set<DynamicQueryRules> getQueries() {
        return queries;
    }

    public void setQueries(Set<DynamicQueryRules> queries) {
        this.queries.clear();
        if (queries != null) {
            this.queries.addAll(queries);
            for (DynamicQueryRules query : queries) {
                query.setDynamicRecommendationRule(this);
            }
        }
    }


}