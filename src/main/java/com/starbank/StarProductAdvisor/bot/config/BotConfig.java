package com.starbank.StarProductAdvisor.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}