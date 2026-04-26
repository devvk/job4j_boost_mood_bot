package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.bmb.model.Mood;

import java.util.List;

public interface MoodRepository extends CrudRepository<Mood, Long> {

    @NonNull
    List<Mood> findAll();
}
