package com.starbank.StarProductAdvisor.rules;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Правило для рекомендации "Top Saving":
 * Проверяет, что у пользователя есть дебетовый продукт,
 * сумма пополнений по дебетовым или сберегательным продуктам больше или равна 50000,
 * и сумма пополнений по дебету больше суммы снятий по дебету.
 */
@Component
public class TopSavingRule implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public TopSavingRule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDTO> apply(UUID userId) {
        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");
        double debitDeposits = repository.sumDepositsByUserAndProductType(userId, "DEBIT");
        double savingDeposits = repository.sumDepositsByUserAndProductType(userId, "SAVING");
        double debitWithdrawals = repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT");

        if (hasDebit && (debitDeposits >= 50000 || savingDeposits >= 50000) && debitDeposits > debitWithdrawals) {
            return Optional.of(new RecommendationDTO(
                    "59efc529-2fff-41af-baff-90ccd7402925",
                    "Top Saving",
                    "Откройте свою собственную «Копилку» с нашим банком!"
            ));
        }
        return Optional.empty();
    }
}