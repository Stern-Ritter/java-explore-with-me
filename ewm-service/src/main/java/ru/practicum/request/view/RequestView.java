package ru.practicum.request.view;

import org.springframework.beans.factory.annotation.Value;
import ru.practicum.request.model.RequestStatus;

import java.time.LocalDateTime;

public interface RequestView {
    Long getId();

    LocalDateTime getCreated();

    @Value("#{target.event != null ? target.event.id : null}")
    Long getEvent();

    @Value("#{target.requester != null ? target.requester.id : null}")
    Long getRequester();

    RequestStatus getStatus();
}
