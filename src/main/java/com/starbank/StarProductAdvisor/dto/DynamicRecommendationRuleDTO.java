package com.starbank.StarProductAdvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.UUID;

public class DynamicRecommendationRuleDTO {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_text")
    private String productText;

    @JsonProperty("rule")
    private Set<DynamicQueryRulesDTO> rule;

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

    public Set<DynamicQueryRulesDTO> getRule() {
        return rule;
    }

    public void setRule(Set<DynamicQueryRulesDTO> rule) {
        this.rule = rule;
    }
}
