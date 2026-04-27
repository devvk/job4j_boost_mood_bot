package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;
import ru.job4j.bmb.service.MoodContentService;

@Component
public class ContentProviderText implements ContentProvider {

    private final MoodContentService moodContentService;

    public ContentProviderText(MoodContentService moodContentService) {
        this.moodContentService = moodContentService;
    }

    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        content.setText(moodContentService.getRandomTextByMoodId(moodId));
        return content;
    }
}
