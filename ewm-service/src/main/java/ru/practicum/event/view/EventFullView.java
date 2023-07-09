package ru.practicum.event.view;

import org.springframework.beans.factory.annotation.Value;
import ru.practicum.category.view.CategoryView;
import ru.practicum.event.model.EventState;
import ru.practicum.location.view.LocationView;
import ru.practicum.user.view.UserFullView;

import java.time.LocalDateTime;

public interface EventFullView {
    Long getId();

    String getAnnotation();

    CategoryView getCategory();

    @Value("#{T(ru.practicum.utils.Utils).countConfirmedRequests(target.requests)}")
    Long getConfirmedRequests();

    LocalDateTime getCreatedOn();

    String getDescription();

    LocalDateTime getEventDate();

    UserFullView getInitiator();

    LocationView getLocation();

    Boolean getPaid();

    Integer getParticipantLimit();

    LocalDateTime getPublishedOn();

    Boolean getRequestModeration();

    EventState getState();

    String getTitle();

    @Value("#{target.likes.size() -  target.dislikes.size()}")
    Integer getRating();
}
