package com.starbank.StarProductAdvisor.integration;

import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CacheBenchmarkTest {

    @Autowired
    private RecommendationsRepository repository;

    @Test
    void benchmarkCachePerformanceAcrossAllMethods() {
        UUID userId = UUID.randomUUID();
        String productType = "DEBIT";

        System.out.println("=== ДИАГНОСТИКА КЭШИРОВАНИЯ ===");
        System.out.println("Соотношение < 1.0 означает, что кэширование НЕ РАБОТАЕТ");

        // Тестируем все методы
        diagnoseCaching("userExists", () -> repository.userExists(userId));
        diagnoseCaching("userHasProductType", () -> repository.userHasProductType(userId, productType));
        diagnoseCaching("sumDeposits", () -> repository.sumDepositsByUserAndProductType(userId, productType));
        diagnoseCaching("sumWithdrawals", () -> repository.sumWithdrawalsByUserAndProductType(userId, productType));
        diagnoseCaching("countTransactions", () -> repository.countTransactionsByUserAndProductType(userId, productType));


    }

    private void diagnoseCaching(String methodName, Runnable methodCall) {
        System.out.println("\n--- " + methodName + " ---");

        // Измеряем базовое время
        long baseTime = measureExecutionTime(methodCall);
        System.out.println("Базовое время: " + TimeUnit.NANOSECONDS.toMicros(baseTime) + " μs");

        // Измеряем несколько повторных вызовов
        long totalCachedTime = 0;
        int cachedCalls = 5;

        for (int i = 1; i <= cachedCalls; i++) {
            long callTime = measureExecutionTime(methodCall);
            double ratio = (double) baseTime / callTime;

            String status = ratio > 1.0 ? "✅" : ratio < 1.0 ? "❌" : "⚠️";
            System.out.println(status + " Вызов " + i + ": " +
                    TimeUnit.NANOSECONDS.toMicros(callTime) + " μs (x" +
                    String.format("%.2f", ratio) + ")");

            totalCachedTime += callTime;
        }

        // Среднее время кэшированных вызовов
        long avgCachedTime = totalCachedTime / cachedCalls;
        double avgRatio = (double) baseTime / avgCachedTime;

        System.out.println("Среднее улучшение: " + String.format("%.2f", avgRatio) + "x");

        if (avgRatio > 1.2) {
            System.out.println("✅ Кэширование РАБОТАЕТ хорошо");
        } else if (avgRatio > 1.0) {
            System.out.println("⚠️  Кэширование работает, но слабо");
        } else {
            System.out.println("❌ Кэширование НЕ РАБОТАЕТ (стало медленнее)");
            fail("Кэширование не работает для метода: " + methodName +
                    ". Соотношение: " + String.format("%.2f", avgRatio) + "x");
        }
    }

    private long measureExecutionTime(Runnable task) {
        // Несколько итераций для стабильности
        long totalTime = 0;
        int iterations = 3;

        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            task.run();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);

            try {
                Thread.sleep(5); // Увеличил паузу
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return totalTime / iterations;
    }
}