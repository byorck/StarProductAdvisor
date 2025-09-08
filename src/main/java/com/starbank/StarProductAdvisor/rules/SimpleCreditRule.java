package com.starbank.StarProductAdvisor.rules;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
/**
 * Правило для рекомендации "Простой кредит":
 * Проверяет, что у пользователя нет кредитного продукта,
 * сумма пополнений по дебетовым продуктам больше суммы снятий,
 * а сумма снятий по дебету превышает 100000.
 */
@Component
public class SimpleCreditRule implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public SimpleCreditRule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDTO> apply(UUID userId) {
        boolean hasCredit = repository.userHasProductType(userId, "CREDIT");
        double debitDeposits = repository.sumDepositsByUserAndProductType(userId, "DEBIT");
        double debitWithdrawals = repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT");

        if (!hasCredit && debitDeposits > debitWithdrawals && debitWithdrawals > 100000) {
            return Optional.of(new RecommendationDTO(
                    "ab138afb-f3ba-4a93-b74f-0fcee86d447f",
                    "Простой кредит",
                    "Откройте мир выгодных кредитов с нами!"
            ));
        }
        return Optional.empty();
    }
}
