package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;

public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {

    @NonNull
    List<MoodLog> findAll();

    List<MoodLog> findByUserAndCreatedAtAfter(User user, long timestamp);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.id NOT IN (
                SELECT ml.user.id
                FROM MoodLog ml
                WHERE ml.createdAt BETWEEN ?1 AND ?2
            )
            """)
    List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay);

    List<MoodLog> findByUserOrderByCreatedAtDesc(User user);

    @Query("""
            SELECT ml
            FROM MoodLog ml
            WHERE ml.user.adviceReminder = true
            AND ml.createdAt = (
                SELECT MAX(innerMl.createdAt)
                FROM MoodLog innerMl
                WHERE innerMl.user = ml.user
            )
            """)
    List<MoodLog> findLatestMoodLogForEachUser();
}
