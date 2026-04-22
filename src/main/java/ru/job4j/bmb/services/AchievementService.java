package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.lang.NonNull;

public class AchievementService implements BeanNameAware {

    @Override
    public void setBeanName(@NonNull String name) {
        System.out.println("Bean name: " + name);
    }

    @PostConstruct
    public void init() {
        System.out.println(getClass().getSimpleName() + " init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println(getClass().getSimpleName() + " destroy.");
    }
}
