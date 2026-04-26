package ru.job4j.bmb.services;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {

    private final MoodLogRepository moodLogRepository;
    private final AwardRepository awardRepository;
    private final AchievementRepository achievementRepository;
    private final SendContent sendContent;

    public AchievementService(MoodLogRepository moodLogRepository, AwardRepository awardRepository,
                              AchievementRepository achievementRepository, SendContent sendContent) {
        this.moodLogRepository = moodLogRepository;
        this.awardRepository = awardRepository;
        this.achievementRepository = achievementRepository;
        this.sendContent = sendContent;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        User user = event.getUser();
        int streak = calculateStreak(user);
        grantAwards(streak, user);
    }

    private void grantAwards(int streak, User user) {
        List<Award> awards = awardRepository.findAll();
        for (Award award : awards) {
            if (award.getDays() <= streak && !achievementRepository.existsByUserAndAward(user, award)) {
                achievementRepository.save(new Achievement(Instant.now().toEpochMilli(), user, award));
                Content content = new Content(user.getChatId());
                content.setText("\uD83C\uDFC6 Новая награда: " + award.getDescription());
                sendContent.send(content);
            }
        }
    }

    private int calculateStreak(User user) {
        List<MoodLog> moodLogs = moodLogRepository.findByUserOrderByCreatedAtDesc(user);
        int streak = 0;
        LocalDate prevDate = null;
        for (MoodLog moodLog : moodLogs) {
            LocalDate createdDate = Instant.ofEpochMilli(moodLog.getCreatedAt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            // дубликаты за день
            if (createdDate.equals(prevDate)) {
                continue;
            }
            // прервался страйк
            if (!moodLog.getMood().isGood()) {
                break;
            }
            // обновляем страйк
            if (prevDate == null || createdDate.equals(prevDate.minusDays(1))) {
                streak++;
                prevDate = createdDate;
            } else {
                break;
            }
        }
        return streak;
    }
}
