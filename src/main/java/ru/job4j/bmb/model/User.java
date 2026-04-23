package ru.job4j.bmb.model;

public class User {

    private Long id;
    private long clientId;
    private long chatId;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }
}
