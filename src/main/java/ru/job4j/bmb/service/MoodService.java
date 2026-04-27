package ru.job4j.bmb.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.event.MoodSelectedEvent;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class MoodService {

    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.eventPublisher = eventPublisher;
    }

    public Content chooseMood(User user, Long moodId) {
        Mood mood = new Mood();
        mood.setId(moodId);
        moodLogRepository.save(new MoodLog(user, mood, Instant.now().toEpochMilli()));
        eventPublisher.publishEvent(new MoodSelectedEvent(user));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> user = userRepository.findByClientId(clientId);
        if (user.isPresent()) {
            long weekAgo = Instant.now().minus(7, ChronoUnit.DAYS).toEpochMilli();
            List<MoodLog> logs = moodLogRepository.findByUserAndCreatedAtAfter(user.get(), weekAgo);
            content.setText(formatMoodLogs(logs, "Mood logs for week"));
        }
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> user = userRepository.findByClientId(clientId);
        if (user.isPresent()) {
            long monthAgo = Instant.now().minus(30, ChronoUnit.DAYS).toEpochMilli();
            List<MoodLog> logs = moodLogRepository.findByUserAndCreatedAtAfter(user.get(), monthAgo);
            content.setText(formatMoodLogs(logs, "Mood logs for month"));
        }
        return Optional.of(content);
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> user = userRepository.findByClientId(clientId);
        if (user.isPresent()) {
            List<Achievement> achievements = achievementRepository.findByUser(user.get());
            content.setText(formatAchievements(achievements));
        }
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochMilli(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }

    private String formatAchievements(List<Achievement> achievements) {
        if (achievements.isEmpty()) {
            return "Awards: no awards found.";
        }
        var sb = new StringBuilder("Awards:\n");
        achievements.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochMilli(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getAward().getDescription()).append("\n");
        });
        return sb.toString();
    }
}
