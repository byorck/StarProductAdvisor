package com.starbank.StarProductAdvisor.service;

import com.starbank.StarProductAdvisor.dto.DynamicRecommendationRuleDTO;
import com.starbank.StarProductAdvisor.entity.Arguments;
import com.starbank.StarProductAdvisor.entity.DynamicQueryRules;
import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.entity.Query;
import com.starbank.StarProductAdvisor.repository.DynamicRecommendationRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRecommendationRuleService {

    private final DynamicRecommendationRuleRepository dynamicRecommendationRuleRepository;
    private final TransactionService transactionService;

    public DynamicRecommendationRuleService(DynamicRecommendationRuleRepository dynamicRecommendationRuleRepository,
                                            TransactionService transactionService) {
        this.dynamicRecommendationRuleRepository = dynamicRecommendationRuleRepository;
        this.transactionService = transactionService;
    }

    public DynamicRecommendationRule createDynamicRecommendationRule(DynamicRecommendationRuleDTO dto) {
        DynamicRecommendationRule entity = mapDtoToEntity(dto);
        if (entity.getQueries() != null) {
            entity.getQueries().forEach(q -> q.setDynamicRecommendationRule(entity));
        }
        return dynamicRecommendationRuleRepository.save(entity);
    }

    private DynamicRecommendationRule mapDtoToEntity(DynamicRecommendationRuleDTO dto) {
        DynamicRecommendationRule entity = new DynamicRecommendationRule();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setProductText(dto.getProductText());

        Set<DynamicQueryRules> queries = dto.getRule().stream()
                .map(qdto -> {
                    DynamicQueryRules q = new DynamicQueryRules();
                    q.setQuery(qdto.getQuery());
                    q.setArguments(qdto.getArguments());
                    q.setNegate(qdto.isNegate());
                    return q;
                }).collect(Collectors.toSet());

        entity.setQueries(queries);
        return entity;
    }

    public boolean checkRuleForUser(DynamicRecommendationRule rule, UUID userId) {
        for (DynamicQueryRules queryRule : rule.getQueries()) {
            boolean result = checkQueryRule(userId, queryRule);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    public boolean checkQueryRule(UUID userId, DynamicQueryRules queryRule) {
        Query query = queryRule.getQuery();
        List<String> args = queryRule.getArguments();

        boolean result;
        switch (query) {
            case USER_OF:
                result = checkUserOf(userId, queryRule, args);
                break;
            case ACTIVE_USER_OF:
                result = checkActiveUserOf(userId, queryRule, args);
                break;
            case TRANSACTION_SUM_COMPARE:
                result = checkTransactionSumCompare(userId, queryRule, args);
                break;
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW:
                result = checkTransactionSumCompareDepositWithdraw(userId, queryRule, args);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип запроса: " + query);
        }
        return result;
    }

    private boolean checkUserOf(UUID userId, DynamicQueryRules queryRule, List<String> args) {
        if (args.size() != 1) throw new IllegalArgumentException("USER_OF принимает ровно один аргумент");
        Arguments productType = Arguments.valueOf(args.get(0));
        boolean isUser = transactionService.isUserOfProduct(userId, productType);
        return queryRule.isNegate() ? !isUser : isUser;
    }

    private boolean checkActiveUserOf(UUID userId, DynamicQueryRules queryRule, List<String> args) {
        if (args.size() != 1) throw new IllegalArgumentException("ACTIVE_USER_OF принимает ровно один аргумент");
        Arguments productType = Arguments.valueOf(args.get(0));
        boolean isActiveUser = transactionService.isActiveUserOfProduct(userId, productType);
        return queryRule.isNegate() ? !isActiveUser : isActiveUser;
    }

    private boolean checkTransactionSumCompare(UUID userId, DynamicQueryRules queryRule, List<String> args) {
        if (args.size() != 4) throw new IllegalArgumentException("TRANSACTION_SUM_COMPARE принимает 4 аргумента");
        Arguments productType = Arguments.valueOf(args.get(0));
        Arguments transactionType = Arguments.valueOf(args.get(1));
        String operator = args.get(2);
        int compareValue = Integer.parseInt(args.get(3));
        int sum = transactionService.sumTransactions(userId, productType, transactionType);
        boolean result = transactionService.compareSumWithValue(sum, operator, compareValue);
        return queryRule.isNegate() ? !result : result;
    }

    private boolean checkTransactionSumCompareDepositWithdraw(UUID userId, DynamicQueryRules queryRule, List<String> args) {
        if (args.size() != 2)
            throw new IllegalArgumentException("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW принимает 2 аргумента");
        Arguments productType = Arguments.valueOf(args.get(0));
        String operator = args.get(1);
        int depositSum = transactionService.sumTransactions(userId, productType, Arguments.DEPOSIT);
        int withdrawSum = transactionService.sumTransactions(userId, productType, Arguments.WITHDRAW);
        boolean result = transactionService.compareSumWithSum(depositSum, withdrawSum, operator);
        return queryRule.isNegate() ? !result : result;
    }
}
