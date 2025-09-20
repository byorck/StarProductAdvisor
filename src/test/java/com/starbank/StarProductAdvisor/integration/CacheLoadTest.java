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
class CacheLoadTest {

    @Autowired
    private RecommendationsRepository repository;

    @Test
    void shouldHandleMultipleCallsWithoutDatabaseOverhead() {
        UUID userId = UUID.randomUUID();
        int iterations = 100;

        // Первый вызов - замеряем время
        long firstCallTime = measureExecutionTime(() -> {
            repository.userExists(userId);
        });

        // Многократные повторные вызовы
        long totalCachedCallsTime = 0;
        for (int i = 0; i < iterations; i++) {
            long callTime = measureExecutionTime(() -> {
                repository.userExists(userId);
            });
            totalCachedCallsTime += callTime;
        }

        long averageCachedCallTime = totalCachedCallsTime / iterations;

        System.out.println("Тест под нагрузкой (" + iterations + " вызовов):");
        System.out.println("Первый вызов (БД): " + TimeUnit.NANOSECONDS.toMicros(firstCallTime) + " μs");
        System.out.println("Среднее время кэшированного вызова: " +
                TimeUnit.NANOSECONDS.toMicros(averageCachedCallTime) + " μs");
        System.out.println("Общее время всех кэшированных вызовов: " +
                TimeUnit.NANOSECONDS.toMicros(totalCachedCallsTime) + " μs");

        // Проверяем, что среднее время кэшированного вызова значительно меньше
        assertTrue(averageCachedCallTime < firstCallTime / 20,
                "Среднее время кэшированного вызова должно быть минимум в 20 раз меньше");

        // Проверяем, что общее время 100 кэшированных вызовов меньше времени 10 вызовов к БД
        assertTrue(totalCachedCallsTime < firstCallTime * 10,
                "100 кэшированных вызовов должны занимать меньше времени чем 10 вызовов к БД");
    }

    private long measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        return System.nanoTime() - startTime;
    }
}
