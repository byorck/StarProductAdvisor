package com.starbank.StarProductAdvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
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
}
