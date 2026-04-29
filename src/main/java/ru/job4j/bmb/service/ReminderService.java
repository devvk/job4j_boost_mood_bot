package ru.job4j.bmb.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReminderService {

    private final SendContent sendContent;
    private final MoodLogRepository moodLogRepository;
    private final AdviceService adviceService;
    private final TgUI tgUI;

    public ReminderService(SendContent sendContent, MoodLogRepository moodLogRepository,
                           AdviceService adviceService, TgUI tgUI) {
        this.sendContent = sendContent;
        this.moodLogRepository = moodLogRepository;
        this.adviceService = adviceService;
        this.tgUI = tgUI;
    }

    @Scheduled(fixedRateString = "${reminder.mood.period}")
    public void sendMoodReminder() {
        var startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        var endOfDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - 1;
        for (User user : moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay, endOfDay)) {
            Content content = new Content(user.getChatId());
            content.setText("Как настроение?");
            content.setMarkup(tgUI.buildButtons());
            sendContent.send(content);
        }
    }

    @Scheduled(fixedRateString = "${reminder.advice.period}")
    public void sendAdviceReminder() {
        for (MoodLog moodLog : moodLogRepository.findLatestMoodLogForEachUser()) {
            User user = moodLog.getUser();
            Mood mood = moodLog.getMood();
            Content content = new Content(user.getChatId());
            String message = adviceService.getAdviceByMood(mood);
            content.setText("СОВЕТ: " + message);
            sendContent.send(content);
        }
    }
}
