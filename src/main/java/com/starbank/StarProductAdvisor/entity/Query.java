package com.starbank.StarProductAdvisor.entity;

public enum Query {
    USER_OF, // принимает 1 аргумент: тип продукта (DEBIT, CREDIT, INVEST, SAVING)
    ACTIVE_USER_OF, // принимает 1 аргумент: тип продукта (DEBIT, CREDIT, INVEST, SAVING)
    TRANSACTION_SUM_COMPARE, // принимает 4 аргумента:
    // 1 - тип продукта (DEBIT, CREDIT, INVEST, SAVING)
    // 2 - тип транзакции (WITHDRAW, DEPOSIT)
    // 3 - оператор сравнения (>, <, =, >=, <=)
    // 4 - число (int)
    TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW; // принимает 2 аргумента:
    // 1 - тип продукта (DEBIT, CREDIT, INVEST, SAVING)
    // 2 - оператор сравнения (>, <, =, >=, <=)
}
