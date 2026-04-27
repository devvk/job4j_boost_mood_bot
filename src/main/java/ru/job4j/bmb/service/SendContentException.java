package ru.job4j.bmb.service;

public class SendContentException extends RuntimeException {

    public SendContentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
