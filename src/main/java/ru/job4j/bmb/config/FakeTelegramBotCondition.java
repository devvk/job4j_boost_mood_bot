package ru.job4j.bmb.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

public class FakeTelegramBotCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        return !"true".equalsIgnoreCase(context.getEnvironment().getProperty("telegram.mode.real"));
    }
}
