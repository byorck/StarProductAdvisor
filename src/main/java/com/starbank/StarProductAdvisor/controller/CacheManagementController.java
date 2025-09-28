package com.starbank.StarProductAdvisor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class CacheManagementController {

    private static final Logger logger = LoggerFactory.getLogger(CacheManagementController.class);
    private final CacheManager cacheManager;

    public CacheManagementController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Map<String, Object>> clearAllCaches() {
        logger.info("Starting cache clearance process...");

        String[] cacheNames = cacheManager.getCacheNames().toArray(new String[0]);
        logger.info("Found caches to clear: {}", Arrays.toString(cacheNames));

        Map<String, Object> response = new HashMap<>();
        Map<String, String> clearedCaches = new HashMap<>();

        for (String cacheName : cacheNames) {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                clearedCaches.put(cacheName, "cleared");
                logger.info("Cache '{}' has been cleared", cacheName);
            } else {
                clearedCaches.put(cacheName, "not found");
                logger.warn("Cache '{}' not found", cacheName);
            }
        }

        response.put("status", "success");
        response.put("message", String.format("Cleared %d cache(s)", clearedCaches.size()));
        response.put("clearedCaches", clearedCaches);
        response.put("timestamp", java.time.LocalDateTime.now());

        logger.info("Cache clearance completed. Total caches cleared: {}", clearedCaches.size());

        return ResponseEntity.ok(response);
    }
}