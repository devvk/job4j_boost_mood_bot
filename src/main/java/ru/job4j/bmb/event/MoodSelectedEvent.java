package ru.job4j.bmb.event;

import ru.job4j.bmb.model.User;

public record MoodSelectedEvent(User user) {
}