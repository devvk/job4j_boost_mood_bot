package ru.job4j.bmb.repository;

import org.springframework.lang.NonNull;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MoodLogFakeRepository extends CrudRepositoryFake<MoodLog, Long> implements MoodLogRepository {

    @NonNull
    public List<MoodLog> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<MoodLog> findByUserAndCreatedAtAfter(User user, long timestamp) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser().equals(user))
                .filter(moodLog -> moodLog.getCreatedAt() > timestamp)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay) {
        var votedToday = memory.values().stream()
                .filter(moodLog -> moodLog.getCreatedAt() >= startOfDay && moodLog.getCreatedAt() <= endOfDay)
                .map(MoodLog::getUser)
                .collect(Collectors.toSet());

        return memory.values().stream()
                .map(MoodLog::getUser)
                .distinct()
                .filter(user -> !votedToday.contains(user))
                .collect(Collectors.toList());
    }
}
