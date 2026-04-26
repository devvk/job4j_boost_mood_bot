package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.bmb.model.MoodContent;

import java.util.List;

public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {

    @NonNull
    List<MoodContent> findAll();

    List<MoodContent> findByMoodId(Long moodId);
}
