package com.starbank.StarProductAdvisor.service;

import com.starbank.StarProductAdvisor.entity.Arguments;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    private final RecommendationsRepository recommendationsRepository;

    public TransactionService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public boolean isUserOfProduct(UUID userId, Arguments productType) {
        return recommendationsRepository.userHasProductType(userId, productType.name());
    }

    public boolean isActiveUserOfProduct(UUID userId, Arguments productType) {
        int count = recommendationsRepository.countTransactionsByUserAndProductType(userId, productType.name());
        return count >= 5;
    }

    public int sumTransactions(UUID userId, Arguments productType, Arguments transactionType) {
        if (transactionType == Arguments.DEPOSIT) {
            double sum = recommendationsRepository.sumDepositsByUserAndProductType(userId, productType.name());
            return (int) Math.round(sum);
        } else if (transactionType == Arguments.WITHDRAW) {
            double sum = recommendationsRepository.sumWithdrawalsByUserAndProductType(userId, productType.name());
            return (int) Math.round(sum);
        } else {
            throw new IllegalArgumentException("Неизвестный тип транзакции: " + transactionType);
        }
    }

    public boolean compareSumWithValue(int sum, String operator, int value) {
        switch (operator) {
            case ">":
                return sum > value;
            case "<":
                return sum < value;
            case "=":
                return sum == value;
            case ">=":
                return sum >= value;
            case "<=":
                return sum <= value;
            default:
                throw new IllegalArgumentException("Неверный оператор сравнения: " + operator);
        }
    }

    public boolean compareSumWithSum(int sum1, int sum2, String operator) {
        switch (operator) {
            case ">":
                return sum1 > sum2;
            case "<":
                return sum1 < sum2;
            case "=":
                return sum1 == sum2;
            case ">=":
                return sum1 >= sum2;
            case "<=":
                return sum1 <= sum2;
            default:
                throw new IllegalArgumentException("Неверный оператор сравнения: " + operator);
        }
    }
}
