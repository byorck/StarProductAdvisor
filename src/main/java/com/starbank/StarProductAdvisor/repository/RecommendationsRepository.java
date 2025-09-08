package com.starbank.StarProductAdvisor.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
    /**
     * Репозиторий для работы с базой данных рекомендаций через JdbcTemplate.
     * Содержит методы для проверки принадлежности пользователя к продуктам и подсчета сумм транзакций.
     */
@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    /**
     * Проверяет, есть ли у пользователя транзакции по продукту заданного типа.
     * @param userId UUID пользователя
     * @param productType Тип продукта (например, DEBIT, CREDIT)
     * @return true если транзакции есть, иначе false
     */
    public boolean userHasProductType(UUID userId, String productType) {
        String sql = "SELECT COUNT(1) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
        return count != null && count > 0;
    }
    /**
     * Суммирует все транзакции пополнений (DEPOSIT) пользователя по продуктам указанного типа.
     */

    public double sumDepositsByUserAndProductType(UUID userId, String productType) {
        String sql = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'";
        Double sum = jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
        return sum != null ? sum : 0;
    }
    /**
     * Суммирует все транзакции снятий (WITHDRAW) пользователя по продуктам указанного типа.
     */
    public double sumWithdrawalsByUserAndProductType(UUID userId, String productType) {
        String sql = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'";
        Double sum = jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
        return sum != null ? sum : 0;
    }


    ///Данный код является примером и позволяет понять, каким образом можно работать с JdbcTemplate.

    /*
    public int getRandomTransactionAmount(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
                Integer.class,
                userId);
        return result != null ? result : 0;
    }
    */

}
