package ru.job4j.bmb.services;

public class SendContentException extends RuntimeException {

    public SendContentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
