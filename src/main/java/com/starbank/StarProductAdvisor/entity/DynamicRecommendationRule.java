package com.starbank.StarProductAdvisor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
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

    @Schema(description = "ID продукта", example = "147f6a0f-3b91-413b-ab99-87f081d60d5a")
    @Column(name = "product_id")
    private UUID productId;

    @Schema(description = "Название продукта")
    @Column(name = "product_name")
    private String productName;

    @Schema(description = "Описание продукта")
    @Column(name = "product_text")
    private String productText;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DynamicQueryRules> queries = new HashSet<>();

    public DynamicRecommendationRule(Long id, UUID productId, String productName, String productText, Set<DynamicQueryRules> queries) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
        this.queries = queries;
    }

    public DynamicRecommendationRule() {

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
        this.queries = queries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DynamicRecommendationRule that = (DynamicRecommendationRule) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(productName, that.productName) && Objects.equals(productText, that.productText) && Objects.equals(queries, that.queries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, productText, queries);
    }
}
