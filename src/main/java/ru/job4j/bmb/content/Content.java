package ru.job4j.bmb.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class Content {

    private final Long chatId;
    private String text;
    private InputFile photo;
    private InlineKeyboardMarkup markup;
    private InputFile audio;

    public Content(Long chatId) {
        this.chatId = chatId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPhoto(InputFile inputFile) {
        this.photo = inputFile;
    }

    public void setAudio(InputFile inputFile) {
        this.audio = inputFile;
    }

    public void setMarkup(InlineKeyboardMarkup markup) {
        this.markup = markup;
    }
}
