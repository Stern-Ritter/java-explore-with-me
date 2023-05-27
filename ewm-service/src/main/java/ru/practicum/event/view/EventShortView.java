package ru.practicum.event.view;

import org.springframework.beans.factory.annotation.Value;
import ru.practicum.category.view.CategoryView;
import ru.practicum.user.view.UserShortView;

import java.time.LocalDateTime;

public interface EventShortView {
    Long getId();

    String getAnnotation();

    CategoryView getCategory();

    @Value("#{T(ru.practicum.utils.Utils).countConfirmedRequests(target.requests)}")
    Long getConfirmedRequests();

    LocalDateTime getEventDate();

    UserShortView getInitiator();

    Boolean getPaid();

    String getTitle();
}
