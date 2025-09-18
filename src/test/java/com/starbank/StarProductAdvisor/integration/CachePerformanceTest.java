package com.starbank.StarProductAdvisor.integration;

import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CachePerformanceTest {

    @Autowired
    private RecommendationsRepository repository;

    @Autowired
    private CacheManager cacheManager; // Для управления кэшем

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("userCache").clear(); // Очистка кэша перед каждым тестом
    }

    @Test
    void userExists_shouldShowPerformanceImprovementWithCache() {
        UUID userId = UUID.randomUUID();

        // Измеряем время первого вызова (БД + заполнение кэша)
        clearCache();
        long firstCallTime = measureTime(() -> repository.userExists(userId));

        // Измеряем время последующих вызовов (только кэш)
        long cacheTime = measureAverageTime(() -> repository.userExists(userId), 10);

        System.out.println("First call time: " + firstCallTime + " ns");
        System.out.println("Cache time (avg): " + cacheTime + " ns");
        System.out.println("Performance ratio: " + (double) firstCallTime / cacheTime);

        // Проверяем, что кэш значительно быстрее
        assertThat(cacheTime).isLessThan(firstCallTime / 2); // Хотя бы в 2 раза быстрее

        assertFalse(repository.userExists(userId));
    }

    private long measureAverageTime(Runnable operation, int iterations) {
        long totalTime = 0;

        // Прогреваем JVM
        for (int i = 0; i < 3; i++) {
            operation.run();
        }

        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            operation.run();
            totalTime += System.nanoTime() - startTime;

            // Небольшая пауза между измерениями
            try { Thread.sleep(10); } catch (InterruptedException e) { /* ignore */ }
        }

        return totalTime / iterations;
    }

    @Test
    void multipleCalls_shouldUseCache() {
        UUID userId = UUID.randomUUID();

        // Первый вызов - должен пойти в БД
        repository.userExists(userId);

        // Второй и последующие - должны использовать кэш
        verifyCacheUsage(() -> repository.userExists(userId), 5);
    }

    private void verifyCacheUsage(Runnable operation, int iterations) {
        // Первое выполнение (должно быть медленнее - кэш заполняется)
        long firstTime = measureTime(operation);
        System.out.println("First time (DB call): " + firstTime + " ns");

        // Последующие выполнения (должны быть быстрее - данные из кэша)
        long totalCacheTime = 0;

        for (int i = 0; i < iterations - 1; i++) {
            long currentTime = measureTime(operation);
            totalCacheTime += currentTime;

            System.out.println("Cache call #" + (i + 1) + ": " + currentTime + " ns");
            System.out.println("Ratio (first/cache): " + (double) firstTime / currentTime);

            // Проверяем, что время кэшированных вызовов значительно меньше первого
            assertThat(currentTime).isLessThan(firstTime * 2);
        }

        // Среднее время кэшированных вызовов должно быть значительно меньше первого
        long averageCacheTime = totalCacheTime / (iterations - 1);
        System.out.println("Average cache time: " + averageCacheTime + " ns");
        System.out.println("Overall ratio: " + (double) firstTime / averageCacheTime);

        assertThat(averageCacheTime).isLessThan(firstTime / 2);
    }

    private long measureTime(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        return System.nanoTime() - startTime;
    }
}