package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.MoodContentRepository;

import java.util.List;
import java.util.Random;

@Service
public class MoodContentService {

    private final MoodContentRepository moodContentRepository;

    public MoodContentService(MoodContentRepository moodContentRepository) {
        this.moodContentRepository = moodContentRepository;
    }

    public String getRandomTextByMoodId(Long moodId) {
        List<MoodContent> data = moodContentRepository.findByMoodId(moodId);
        if (!data.isEmpty()) {
            Random random = new Random();
            return data.get(random.nextInt(data.size())).getText();
        }
        return "Для этого настроения пока нет рекомендации.";
    }
}
