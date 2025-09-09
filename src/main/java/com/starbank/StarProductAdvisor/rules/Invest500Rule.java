package com.starbank.StarProductAdvisor.rules;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Правило для рекомендации 'Invest 500':
 * Проверяет, есть ли у пользователя дебетовый счет, нет инвестиционного счета,
 * и сумма пополнений по сберегательным продуктам больше 1000.
 */
@Component
public class Invest500Rule implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public Invest500Rule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDTO> apply(UUID userId) {
        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");
        boolean hasInvest = repository.userHasProductType(userId, "INVEST");
        double savingDeposits = repository.sumDepositsByUserAndProductType(userId, "SAVING");

        if (hasDebit && !hasInvest && savingDeposits > 1000) {
            return Optional.of(new RecommendationDTO(
                    "147f6a0f-3b91-413b-ab99-87f081d60d5a",
                    "Invest 500",
                    "Путь к успеху с индивидуальным инвестиционным счетом от банка."
            ));
        }
        return Optional.empty();
    }
}