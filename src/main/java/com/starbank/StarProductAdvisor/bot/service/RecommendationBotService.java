package com.starbank.StarProductAdvisor.bot.service;

import com.starbank.StarProductAdvisor.bot.config.BotConfig;
import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.exception.UserNotFoundException;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import com.starbank.StarProductAdvisor.service.RecommendationService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationBotService {

    private final RecommendationService recommendationService;
    private final RecommendationsRepository recommendationsRepository;
    private final BotConfig botConfig;

    public RecommendationBotService(
            RecommendationService recommendationService,
            RecommendationsRepository recommendationsRepository,
            BotConfig botConfig) {
        this.recommendationService = recommendationService;
        this.recommendationsRepository = recommendationsRepository;
        this.botConfig = botConfig;
    }

    public SendMessage processStartMessage(Long chatId) {
        String welcome = "Здравствуйте! Я " + botConfig.getName() + ".\n" +
                "Используйте команду:\n" +
                "/recommend username";
        return new SendMessage(chatId.toString(), welcome);
    }

    public SendMessage getRecommendationsMessage(String username, Long chatId) {
        try {
            UUID userId = recommendationsRepository.getUserIdByUsername(username);
            List<RecommendationDTO> recommendations = recommendationService.getRecommendationsForUser(userId);

            if (recommendations.isEmpty()) {
                return new SendMessage(chatId.toString(),
                        "Нет новых рекомендаций для пользователя " + username);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Здравствуйте ").append(username).append("!\n\nНовые продукты для вас:\n");
            for (RecommendationDTO r : recommendations) {
                sb.append("- ").append(r.getName()).append(": ").append(r.getText()).append("\n");
            }

            return new SendMessage(chatId.toString(), sb.toString());

        } catch (UserNotFoundException e) {
            return new SendMessage(chatId.toString(), "Пользователь не найден");
        } catch (Exception e) {
            e.printStackTrace();
            return new SendMessage(chatId.toString(), "Произошла ошибка при получении рекомендаций");
        }
    }
}