package com.starbank.StarProductAdvisor.entity;

public enum Arguments {
    DEBIT,
    CREDIT,
    INVEST,
    SAVING,
    WITHDRAW,
    DEPOSIT,
    GREATER(">"),
    LESS("<"),
    EQUAL("="),
    GREATER_OR_EQUAL(">="),
    LESS_OR_EQUAL("<=");

    private final String symbol;

    Arguments() {
        this.symbol = this.name();
    }

    Arguments(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
