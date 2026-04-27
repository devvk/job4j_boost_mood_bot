package ru.job4j.bmb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.config.FakeTelegramBotCondition;
import ru.job4j.bmb.content.Content;

@Service
@Conditional(FakeTelegramBotCondition.class)
public class FakeTelegramBotService extends TelegramLongPollingBot implements SendContent {

    private final String botName;
    private final BotCommandHandler botCommandHandler;

    public FakeTelegramBotService(@Value("${telegram.bot.name}") String botName,
                                  @Value("${telegram.bot.token}") String botToken,
                                  BotCommandHandler botCommandHandler) {
        super(botToken);
        this.botName = botName;
        this.botCommandHandler = botCommandHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            botCommandHandler.handleCallback(update.getCallbackQuery()).ifPresent(this::send);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            botCommandHandler.commands(update.getMessage()).ifPresent(this::send);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void send(Content content) {
        System.out.println(content.getText());
    }
}
