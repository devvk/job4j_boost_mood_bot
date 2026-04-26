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
                SELECT m.user.id
                FROM MoodLog m
                WHERE m.createdAt BETWEEN ?1 AND ?2
            )
            """)
    List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay);
}
