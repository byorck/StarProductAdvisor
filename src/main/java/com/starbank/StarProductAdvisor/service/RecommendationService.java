package com.starbank.StarProductAdvisor.service;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.entity.Arguments;
import com.starbank.StarProductAdvisor.entity.DynamicQueryRules;
import com.starbank.StarProductAdvisor.entity.DynamicRecommendationRule;
import com.starbank.StarProductAdvisor.entity.Query;
import com.starbank.StarProductAdvisor.exception.DatabaseException;
import com.starbank.StarProductAdvisor.exception.UserNotFoundException;
import com.starbank.StarProductAdvisor.repository.DynamicRecommendationRuleRepository;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import com.starbank.StarProductAdvisor.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис рекомендаций.
 * Хранит список всех правил RecommendationRuleSet и применяет их к пользователю,
 * собирая все подходящие рекомендации.
 */
@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final RecommendationsRepository repository;
    private final DynamicRecommendationRuleRepository dynamicRecommendationRuleRepository;
    private final TransactionService transactionService;
    private final StatisticService statisticService;

    public RecommendationService(List<RecommendationRuleSet> ruleSets, RecommendationsRepository repository, DynamicRecommendationRuleRepository dynamicRecommendationRuleRepository, TransactionService transactionService, StatisticService statisticService) {
        this.ruleSets = ruleSets;
        this.repository = repository;
        this.dynamicRecommendationRuleRepository = dynamicRecommendationRuleRepository;
        this.transactionService = transactionService;
        this.statisticService = statisticService;
    }


    /**
     * Получает список рекомендаций для пользователя по его UUID.
     * Выполняет предварительную проверку, существует ли пользователь в базе.
     * Если пользователь не найден, выбрасывается {@link UserNotFoundException}.
     * Перебирает все подключённые правила рекомендаций и собирает подходящие в список.
     *
     * @param userId UUID пользователя, для которого ищутся рекомендации
     * @return список рекомендаций {@link RecommendationDTO}, может быть пустым
     * @throws UserNotFoundException если пользователь с заданным userId не найден
     * @throws DatabaseException     в случае технических неполадок при обращении к базе данных
     */
// Возвращает рекомендации как из статических, так и из динамических правил
    public List<RecommendationDTO> getRecommendationsForUser(UUID userId) {
        if (!repository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<RecommendationDTO> recommendations = new ArrayList<>();

        // Статические
        for (RecommendationRuleSet rule : ruleSets) {
            rule.apply(userId).ifPresent(recommendations::add);
        }

        // Динамические
        List<DynamicRecommendationRule> dynamicRules = getAllDynamicRecommendationRules();
        for (DynamicRecommendationRule dynamicRule : dynamicRules) {
            applyDynamicRule(dynamicRule, userId).ifPresent(recommendations::add);
        }

        return recommendations;
    }

    // Загрузка всех динамических правил из БД
    public List<DynamicRecommendationRule> getAllDynamicRecommendationRules() {
        return dynamicRecommendationRuleRepository.findAll();
    }

    // Проверяет динамическое правило для userId и возвращает RecommendationDTO, если условие выполнено
    public Optional<RecommendationDTO> applyDynamicRule(DynamicRecommendationRule rule, UUID userId) {
        Set<DynamicQueryRules> queries = rule.getQueries();

        for (DynamicQueryRules queryRule : queries) {
            boolean result = evaluateQueryRule(queryRule, userId);

            // Учитываем логическое отрицание
            if (queryRule.isNegate()) {
                result = !result;
            }
            if (!result) {
                // Если хотя бы одно условие не выполнено — правило не подходит
                return Optional.empty();
            }
        }

        // Увеличиваем счётчик срабатываний для данной рекомендации
        statisticService.incrementRecommendationCount(rule);

        RecommendationDTO dto = new RecommendationDTO(
                rule.getProductId().toString(),
                rule.getProductName(),
                rule.getProductText()
        );

        return Optional.of(dto);
    }

    // Оценка одного условия динамического правила
    private boolean evaluateQueryRule(DynamicQueryRules queryRule, UUID userId) {
        Query queryType = queryRule.getQuery();
        List<String> args = queryRule.getArguments();

        switch (queryType) {
            case USER_OF:
                // Проверяем наличие у пользователя продукта args[0]
                return repository.userHasProductType(userId, args.get(0));

            case ACTIVE_USER_OF:
                // Проверяем активность пользователя по продукту args[0]
                return transactionService.isActiveUserOfProduct(userId, Arguments.valueOf(args.get(0)));

            case TRANSACTION_SUM_COMPARE: {
                String productType1 = args.get(0);
                String secondArg = args.get(1);
                String operator = args.get(2);
                int value = Integer.parseInt(args.get(3));

                if ("DEPOSIT".equalsIgnoreCase(secondArg) || "WITHDRAW".equalsIgnoreCase(secondArg)) {
                    // Если второй аргумент - тип транзакции
                    int sum = transactionService.sumTransactions(userId,
                            Arguments.valueOf(productType1),
                            Arguments.valueOf(secondArg));
                    return transactionService.compareSumWithValue(sum, operator, value);
                } else {
                    // Иначе сравниваем суммы по двум продуктам
                    int sumProduct1 = (int) Math.round(repository.sumDepositsByUserAndProductType(userId, productType1));
                    int sumProduct2 = (int) Math.round(repository.sumDepositsByUserAndProductType(userId, secondArg));
                    return transactionService.compareSumWithSum(sumProduct1, sumProduct2, operator);
                }
            }

            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW: {
                String productType = args.get(0);
                String operator = args.get(1);
                int deposits = transactionService.sumTransactions(userId,
                        Arguments.valueOf(productType),
                        Arguments.DEPOSIT);
                int withdrawals = transactionService.sumTransactions(userId,
                        Arguments.valueOf(productType),
                        Arguments.WITHDRAW);
                return transactionService.compareSumWithSum(deposits, withdrawals, operator);
            }

            default:
                throw new IllegalArgumentException("Неизвестный тип запроса: " + queryType);
        }
    }
}