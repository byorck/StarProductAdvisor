package com.starbank.StarProductAdvisor.dto;
/**
  DTO для передачи информации о конкретной рекомендации.
  Содержит id продукта, имя и текст описания.
 */
public class RecommendationDTO {
    private String id;
    private String name;
    private String text;

    public RecommendationDTO(String id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}