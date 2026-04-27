package ru.job4j.bmb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.config.RealTelegramBotCondition;
import ru.job4j.bmb.content.Content;

@Service
@Conditional(RealTelegramBotCondition.class)
public class TelegramBotService extends TelegramLongPollingBot implements SendContent {

    private final String botName;
    private final BotCommandHandler botCommandHandler;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
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
        try {
            if (content.getAudio() != null) {
                SendAudio message = new SendAudio();
                message.setChatId(content.getChatId());
                message.setAudio(content.getAudio());
                if (content.getText() != null) {
                    message.setCaption(content.getText());
                }
                execute(message);
            } else if (content.getPhoto() != null) {
                SendPhoto message = new SendPhoto();
                message.setChatId(content.getChatId());
                message.setPhoto(content.getPhoto());
                if (content.getText() != null) {
                    message.setCaption(content.getText());
                }
                execute(message);
            } else if (content.getText() != null) {
                SendMessage message = new SendMessage();
                message.setChatId(content.getChatId());
                message.setText(content.getText());
                if (content.getMarkup() != null) {
                    message.setReplyMarkup(content.getMarkup());
                }
                execute(message);
            }
        } catch (TelegramApiException e) {
            throw new SendContentException(e.getMessage(), e);
        }
    }
}
