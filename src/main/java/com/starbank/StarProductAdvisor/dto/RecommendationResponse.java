package com.starbank.StarProductAdvisor.dto;

import java.util.List;

/**
 * DTO для формирования ответа на запрос рекомендаций.
 * Содержит идентификатор пользователя и список рекомендаций.
 */
public class RecommendationResponse {
    private String user_id;
    private List<RecommendationDTO> recommendations;

    public RecommendationResponse(String user_id, List<RecommendationDTO> recommendations) {
        this.user_id = user_id;
        this.recommendations = recommendations;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<RecommendationDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationDTO> recommendations) {
        this.recommendations = recommendations;
    }
}