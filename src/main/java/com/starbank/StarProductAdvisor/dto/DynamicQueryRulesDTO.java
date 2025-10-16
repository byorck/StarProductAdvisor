package com.starbank.StarProductAdvisor.dto;

import com.starbank.StarProductAdvisor.entity.Query;

import java.util.List;
import java.util.Objects;

public class DynamicQueryRulesDTO {
    private Query query;
    private List<String> arguments;
    private boolean negate;


    public DynamicQueryRulesDTO() {
    }

    public DynamicQueryRulesDTO(Query query, List<String> arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicQueryRulesDTO that = (DynamicQueryRulesDTO) o;
        return negate == that.negate &&
                query == that.query &&
                Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, arguments, negate);
    }

    @Override
    public String toString() {
        return "DynamicQueryRulesDTO{" +
                "query=" + query +
                ", arguments=" + arguments +
                ", negate=" + negate +
                '}';
    }
}