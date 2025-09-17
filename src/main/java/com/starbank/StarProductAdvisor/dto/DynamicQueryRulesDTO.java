package com.starbank.StarProductAdvisor.dto;

import com.starbank.StarProductAdvisor.entity.Query;

import java.util.List;
public class DynamicQueryRulesDTO {

    private Query query;

    private List<String> arguments;

    private boolean negate;

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
}
