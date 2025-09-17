package com.starbank.StarProductAdvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class DynamicRecommendationRuleWithoutIdDTO {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_text")
    private String productText;

    @JsonProperty("rule")
    private List<DynamicQueryRulesDTO> rule;
    @NotBlank
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @NotNull
    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    @NotBlank
    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    @NotEmpty
    public List<DynamicQueryRulesDTO> getRule() {
        return rule;
    }

    public void setRule(List<DynamicQueryRulesDTO> rule) {
        this.rule = rule;
    }
}
