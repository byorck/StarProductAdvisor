package com.starbank.StarProductAdvisor.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Query {
    USER_OF,
    ACTIVE_USER_OF,
    TRANSACTION_SUM_COMPARE,
    TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static Query fromValue(String value) {
        for (Query q : Query.values()) {
            if (q.name().equalsIgnoreCase(value)) {
                return q;
            }
        }
        throw new IllegalArgumentException("Unknown query value: " + value);
    }
}
