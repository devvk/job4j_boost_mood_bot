package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.User;

import java.util.List;

public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    @NonNull
    List<Achievement> findAll();

    List<Achievement> findByUser(User user);
}
