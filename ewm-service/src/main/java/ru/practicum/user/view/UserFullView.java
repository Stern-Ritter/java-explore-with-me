package ru.practicum.user.view;

import org.springframework.beans.factory.annotation.Value;

public interface UserFullView {
    Long getId();

    String getName();

    String getEmail();

    @Value("#{T(ru.practicum.utils.Utils).calculateRating(target.events)}")
    Long getRating();
}
