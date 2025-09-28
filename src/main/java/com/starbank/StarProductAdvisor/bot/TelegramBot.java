package com.starbank.StarProductAdvisor.bot;

import com.starbank.StarProductAdvisor.bot.service.RecommendationBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final RecommendationBotService recommendationBotService;

    public TelegramBot(RecommendationBotService recommendationBotService) {
        this.recommendationBotService = recommendationBotService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            SendMessage response;

            if (messageText.equals("/start")) {
                response = recommendationBotService.processStartMessage(chatId);
            } else if (messageText.startsWith("/recommend ")) {
                String username = messageText.substring("/recommend ".length()).trim();
                response = recommendationBotService.getRecommendationsMessage(username, chatId);
            } else {
                response = new SendMessage(chatId.toString(),
                        "Неизвестная команда. Используйте /recommend <username>");
            }

            sendMessage(response);
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}