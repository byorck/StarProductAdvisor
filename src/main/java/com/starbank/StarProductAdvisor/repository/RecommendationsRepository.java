package com.starbank.StarProductAdvisor.repository;

import com.starbank.StarProductAdvisor.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
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
     * Проверяет, существует ли пользователь с таким ID.
     *
     * @param userId UUID пользователя
     * @return true если пользователь есть, иначе false
     * @throws DatabaseException при технической ошибке базы
     */
    public boolean userExists(UUID userId) {
        try {
            String sql = "SELECT COUNT(1) FROM users WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
            return count != null && count > 0;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Ошибка при проверке существования пользователя", ex);
        }
    }

    /**
     * Проверяет, есть ли у пользователя транзакции по продукту заданного типа.
     *
     * @param userId      UUID пользователя
     * @param productType Тип продукта (например, DEBIT, CREDIT)
     * @return true если транзакции есть, иначе false
     * @throws DatabaseException при технической ошибке базы
     */
    public boolean userHasProductType(UUID userId, String productType) {
        try {
            String sql = "SELECT COUNT(1) FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = ? AND p.type = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
            return count != null && count > 0;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Ошибка в userHasProductType", ex);
        }
    }

    /**
     * Суммирует все транзакции пополнений (DEPOSIT) пользователя по продуктам указанного типа.
     *
     * @param userId      UUID пользователя
     * @param productType Тип продукта (например, DEBIT, CREDIT)
     * @return сумма транзакций пополнений
     * @throws DatabaseException при технической ошибке базы
     */
    public double sumDepositsByUserAndProductType(UUID userId, String productType) {
        try {
            String sql = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'";
            Double sum = jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
            return sum != null ? sum : 0;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Ошибка в sumDepositsByUserAndProductType", ex);
        }
    }

    /**
     * Суммирует все транзакции снятий (WITHDRAW) пользователя по продуктам указанного типа.
     *
     * @param userId      UUID пользователя
     * @param productType Тип продукта (например, DEBIT, CREDIT)
     * @return сумма транзакций снятий
     * @throws DatabaseException при технической ошибке базы
     */
    public double sumWithdrawalsByUserAndProductType(UUID userId, String productType) {
        try {
            String sql = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'";
            Double sum = jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
            return sum != null ? sum : 0;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Ошибка в sumWithdrawalsByUserAndProductType", ex);
        }
    }

    /**
     * Считает количество транзакций у пользователя по типу.
     *
     * @param userId      UUID пользователя
     * @param productType Тип продукта (например, DEBIT, CREDIT)
     * @return количество транзакций
     * @throws DatabaseException при технической ошибке базы
     */
    public int countTransactionsByUserAndProductType(UUID userId, String productType) {
        try {
            String sql = "SELECT COUNT(1) FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = ? AND p.type = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
            return count != null ? count : 0;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Ошибка при подсчёте транзакций пользователя", ex);
        }
    }
}
