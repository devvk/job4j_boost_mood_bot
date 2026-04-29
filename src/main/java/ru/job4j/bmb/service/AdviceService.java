package ru.job4j.bmb.service;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AdviceService {

    private final UserRepository userRepository;

    private final Random random = new Random();

    private final List<String> goodAdvices = List.of(
            "Отличное настроение — используй его для маленькой победы сегодня.",
            "Сегодня хороший день, чтобы сделать то, что ты давно откладывал.",
            "Поделись своим хорошим настроением с кем-то рядом.",
            "Закрепи это состояние: прогулка, спорт или что-то полезное.",
            "Ты на волне — используй это для роста.",
            "Сделай сегодня чуть больше, чем обычно — у тебя получится.",
            "Хорошее настроение — это ресурс. Потрать его с умом.",
            "Попробуй сегодня что-то новое — сейчас лучший момент.",
            "Ты уже на правильном пути, продолжай.",
            "Зафиксируй этот момент и запомни его."
    );

    private final List<String> badAdvices = List.of(
            "Сделай паузу и сделай несколько глубоких вдохов.",
            "Не требуй от себя слишком многого сегодня.",
            "Один маленький шаг — уже прогресс.",
            "Попробуй короткую прогулку без телефона.",
            "Чашка чая и тишина могут помочь восстановиться.",
            "Это состояние временное — оно пройдет.",
            "Сконцентрируйся на чем-то простом и понятном.",
            "Дай себе время — не нужно спешить.",
            "Попробуй переключиться на физическую активность.",
            "Ты справлялся раньше — справишься и сейчас."
    );

    public AdviceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAdviceByMood(Mood mood) {
        List<String> source = mood.isGood() ? goodAdvices : badAdvices;
        if (source.isEmpty()) {
            return "Сегодня просто будь к себе внимателен.";
        }
        return source.get(random.nextInt(source.size()));
    }

    private String getRandomAdvice() {
        List<String> all = new ArrayList<>();
        all.addAll(goodAdvices);
        all.addAll(badAdvices);
        return all.get(random.nextInt(all.size()));
    }

    public Optional<Content> dailyAdviceCommand(long chatId) {
        Content content = new Content(chatId);
        content.setText("СОВЕТ: " + getRandomAdvice());
        return Optional.of(content);
    }

    public Optional<Content> adviceOn(long chatId, Long clientId) {
        User user = userRepository.findByClientId(clientId).orElseThrow();
        user.setAdviceReminder(true);
        userRepository.save(user);
        Content content = new Content(chatId);
        content.setText("Ежедневные советы включены.");
        return Optional.of(content);
    }

    public Optional<Content> adviceOff(long chatId, Long clientId) {
        User user = userRepository.findByClientId(clientId).orElseThrow();
        user.setAdviceReminder(false);
        userRepository.save(user);
        Content content = new Content(chatId);
        content.setText("Ежедневные советы отключены.");
        return Optional.of(content);
    }
}
