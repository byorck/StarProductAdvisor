package com.starbank.StarProductAdvisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbank.StarProductAdvisor.controller.DynamicRecommendationRuleController;
import com.starbank.StarProductAdvisor.dto.DynamicQueryRulesDTO;
import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleWithIdDTO;
import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleWithoutIdDTO;
import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.entity.Query;
import com.starbank.StarProductAdvisor.service.DynamicRecommendationRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DynamicRecommendationRuleController.class)
class DynamicRecommendationRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DynamicRecommendationRuleService dynamicRecommendationRuleService;

    private final UUID testProductId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");


    @Test
    void createDynamicRecommendationRule_ValidData_Returns200Ok() throws Exception {

        String validJson = """
        {
            "product_name": "Test Product",
            "product_id": "550e8400-e29b-41d4-a716-446655440000",
            "product_text": "Product description",
            "rule": [
                {
                    "query": "USER_OF",
                    "arguments": ["DEBIT"],
                    "negate": false
                }
            ]
        }
        """;

        DynamicRecommendationRule entity = new DynamicRecommendationRule();
        entity.setId(1L);
        entity.setProductName("Test Product");
        entity.setProductId(testProductId);

        DynamicRecommendationRuleWithIdDTO responseDto = new DynamicRecommendationRuleWithIdDTO();
        responseDto.setId(1L);
        responseDto.setProductName("Test Product");
        responseDto.setProductId(testProductId);
        responseDto.setProductText("Product description");

        DynamicQueryRulesDTO queryRule = new DynamicQueryRulesDTO();
        queryRule.setQuery(Query.USER_OF);
        queryRule.setArguments(List.of("DEBIT"));
        queryRule.setNegate(false);

        responseDto.setRule(List.of(queryRule));

        when(dynamicRecommendationRuleService.createDynamicRecommendationRule(any(DynamicRecommendationRuleWithoutIdDTO.class))).thenReturn(entity);
        when(dynamicRecommendationRuleService.mapWithIdEntityToDto(entity)).thenReturn(responseDto);


        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.product_name").value("Test Product"))
                .andExpect(jsonPath("$.product_id").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.rule[0].query").value("USER_OF"));

        verify(dynamicRecommendationRuleService, times(1)).createDynamicRecommendationRule(any(DynamicRecommendationRuleWithoutIdDTO.class));
    }

    @Test
    void getAllDynamicRecommendationRule_WithRules_ReturnsValidJsonStructure() throws Exception {
        // Given
        DynamicRecommendationRuleWithIdDTO rule1 = new DynamicRecommendationRuleWithIdDTO();
        rule1.setId(1L);
        rule1.setProductName("Product 1");
        rule1.setProductId(testProductId);
        rule1.setProductText("Description 1");

        DynamicQueryRulesDTO queryRule1 = new DynamicQueryRulesDTO();
        queryRule1.setQuery(Query.USER_OF);
        queryRule1.setArguments(List.of("DEBIT"));
        queryRule1.setNegate(false);
        rule1.setRule(List.of(queryRule1));

        when(dynamicRecommendationRuleService.getAllDynamicRecommendationRuleDto())
                .thenReturn(List.of(rule1));


        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].product_name").value("Product 1"))
                .andExpect(jsonPath("$.data[0].product_id").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.data[0].rule[0].query").value("USER_OF"));
    }

    @Test
    void getAllDynamicRecommendationRule_EmptyList_ReturnsEmptyArray() throws Exception {

        when(dynamicRecommendationRuleService.getAllDynamicRecommendationRuleDto())
                .thenReturn(Collections.emptyList());


        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void getDynamicRecommendationRule_ExistingId_Returns200WithData() throws Exception {

        DynamicRecommendationRuleWithIdDTO rule = new DynamicRecommendationRuleWithIdDTO();
        rule.setId(1L);
        rule.setProductName("Test Product");
        rule.setProductId(testProductId);
        rule.setProductText("Description");

        DynamicQueryRulesDTO queryRule = new DynamicQueryRulesDTO();
        queryRule.setQuery(Query.ACTIVE_USER_OF);
        queryRule.setArguments(List.of("CREDIT"));
        queryRule.setNegate(false);
        rule.setRule(List.of(queryRule));

        when(dynamicRecommendationRuleService.getDynamicRecommendationRuleDto(1L))
                .thenReturn(Optional.of(rule));


        mockMvc.perform(get("/rule/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.product_name").value("Test Product"))
                .andExpect(jsonPath("$.product_id").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.rule[0].query").value("ACTIVE_USER_OF"));
    }

    @Test
    void getDynamicRecommendationRule_NonExistingId_Returns200WithNull() throws Exception {

        when(dynamicRecommendationRuleService.getDynamicRecommendationRuleDto(999L))
                .thenReturn(Optional.empty());


        mockMvc.perform(get("/rule/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist()); // Проверяем что body пустое

        verify(dynamicRecommendationRuleService, times(1)).getDynamicRecommendationRuleDto(999L);
    }

    @Test
    void deleteDynamicRecommendationRule_ExistingId_Returns204NoContent() throws Exception {

        when(dynamicRecommendationRuleService.deleteDynamicRecommendationRule(1L))
                .thenReturn(ResponseEntity.noContent().build());


        mockMvc.perform(delete("/rule/1"))
                .andExpect(status().isNoContent());

        verify(dynamicRecommendationRuleService, times(1)).deleteDynamicRecommendationRule(1L);
    }

    @Test
    void deleteDynamicRecommendationRule_NonExistingId_Returns404NotFound() throws Exception {

        when(dynamicRecommendationRuleService.deleteDynamicRecommendationRule(999L))
                .thenReturn(ResponseEntity.notFound().build());


        mockMvc.perform(delete("/rule/999"))
                .andExpect(status().isNotFound());

        verify(dynamicRecommendationRuleService, times(1)).deleteDynamicRecommendationRule(999L);
    }


    @Test
    void createDynamicRecommendationRule_EmptyRuleArray_Returns400BadRequest() throws Exception {

        String invalidJson = """
        {
            "product_name": "Test Product",
            "product_id": "550e8400-e29b-41d4-a716-446655440000",
            "product_text": "Description",
            "rule": []
        }
        """;


        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.details.rule").value("must not be empty"));

        verify(dynamicRecommendationRuleService, never()).createDynamicRecommendationRule(any());
    }

    @Test
    void createDynamicRecommendationRule_MissingRequiredFields_Returns400BadRequest() throws Exception {

        String invalidJson = """
        {
            "product_id": "550e8400-e29b-41d4-a716-446655440000",
            "product_text": "Description",
            "rule": [
                {
                    "query": "USER_OF",
                    "arguments": ["DEBIT"],
                    "negate": false
                }
            ]
        }
        """;


        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.details.productName").value("must not be blank"));

        verify(dynamicRecommendationRuleService, never()).createDynamicRecommendationRule(any());
    }


    @Test
    void createDynamicRecommendationRule_InvalidQueryValue_Returns500InternalError() throws Exception {
        String invalidJson = """
        {
            "product_name": "Test Product",
            "product_id": "550e8400-e29b-41d4-a716-446655440000",
            "product_text": "Description",
            "rule": [
                {
                    "query": "INVALID_QUERY",
                    "arguments": ["DEBIT"],
                    "negate": false
                }
            ]
        }
        """;


        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").isString());

        verify(dynamicRecommendationRuleService, never()).createDynamicRecommendationRule(any());
    }

    @Test
    void deleteDynamicRecommendationRule_InvalidIdFormat_Returns500InternalError() throws Exception {

        mockMvc.perform(delete("/rule/abc"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").isString());

        verify(dynamicRecommendationRuleService, never()).deleteDynamicRecommendationRule(any());
    }
}
