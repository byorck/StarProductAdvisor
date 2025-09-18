package com.starbank.StarProductAdvisor.integration;

import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CacheBenchmarkTest {

    @Autowired
    private RecommendationsRepository repository;

    @Test
    void benchmarkCachePerformanceAcrossAllMethods() {
        UUID userId = UUID.randomUUID();
        String productType = "DEBIT";

        System.out.println("=== БЕНЧМАРК ПРОИЗВОДИТЕЛЬНОСТИ КЭШИРОВАНИЯ ===");

        // Тестируем все методы
        benchmarkMethod("userExists", () -> repository.userExists(userId));
        benchmarkMethod("userHasProductType", () -> repository.userHasProductType(userId, productType));
        benchmarkMethod("sumDeposits", () -> repository.sumDepositsByUserAndProductType(userId, productType));
        benchmarkMethod("sumWithdrawals", () -> repository.sumWithdrawalsByUserAndProductType(userId, productType));
        benchmarkMethod("countTransactions", () -> repository.countTransactionsByUserAndProductType(userId, productType));
    }

    private void benchmarkMethod(String methodName, Runnable methodCall) {
        System.out.println("\n--- " + methodName + " ---");

        // Первый вызов (БД)
        long firstCallTime = measureExecutionTime(() -> {
            methodCall.run();
        });
        System.out.println("Первый вызов (БД): " + TimeUnit.NANOSECONDS.toMicros(firstCallTime) + " μs");

        // Второй вызов (кэш)
        long secondCallTime = measureExecutionTime(() -> {
            methodCall.run();
        });
        System.out.println("Второй вызов (кэш): " + TimeUnit.NANOSECONDS.toMicros(secondCallTime) + " μs");

        // Третий вызов (кэш)
        long thirdCallTime = measureExecutionTime(() -> {
            methodCall.run();
        });
        System.out.println("Третий вызов (кэш): " + TimeUnit.NANOSECONDS.toMicros(thirdCallTime) + " μs");

        // Проверяем улучшение производительности
        double improvementRatio = (double) firstCallTime / secondCallTime;
        System.out.println("Улучшение производительности: " + String.format("%.1f", improvementRatio) + "x");

        assertTrue(improvementRatio > 5,
                "Кэширование должно улучшить производительность минимум в 5 раз для " + methodName +
                        ". Соотношение: " + String.format("%.1f", improvementRatio) + "x");

        // Проверяем стабильность кэшированных вызовов
        assertTrue(Math.abs(secondCallTime - thirdCallTime) < firstCallTime / 50,
                "Время повторных кэшированных вызовов должно быть стабильным");
    }

    private long measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        return System.nanoTime() - startTime;
    }
}
