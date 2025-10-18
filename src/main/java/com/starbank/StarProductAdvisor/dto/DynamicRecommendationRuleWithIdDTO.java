package com.starbank.StarProductAdvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DynamicRecommendationRuleWithIdDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_text")
    private String productText;

    @JsonProperty("rule")
    private List<DynamicQueryRulesDTO> rule;


    public DynamicRecommendationRuleWithIdDTO() {
    }

    public DynamicRecommendationRuleWithIdDTO(Long id, String productName, UUID productId,
                                              String productText, List<DynamicQueryRulesDTO> rule) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<DynamicQueryRulesDTO> getRule() {
        return rule;
    }

    public void setRule(List<DynamicQueryRulesDTO> rule) {
        this.rule = rule;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicRecommendationRuleWithIdDTO that = (DynamicRecommendationRuleWithIdDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(productText, that.productText) &&
                Objects.equals(rule, that.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productId, productText, rule);
    }

    @Override
    public String toString() {
        return "DynamicRecommendationRuleWithIdDTO{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productId=" + productId +
                ", productText='" + productText + '\'' +
                ", rule=" + rule +
                '}';
    }
}