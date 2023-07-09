package ru.practicum.event.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventService;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class RatingEventController {
    private static final String USER_ID_HTTP_HEADER = "X-User-Id";
    private final EventService eventService;

    @PatchMapping("/{eventId}/like")
    public EventFullDto like(@RequestHeader(USER_ID_HTTP_HEADER) Long userId, @PathVariable Long eventId) {
        return eventService.like(userId, eventId);
    }

    @PatchMapping("/{eventId}/dislike")
    public EventFullDto dislike(@RequestHeader(USER_ID_HTTP_HEADER) Long userId, @PathVariable Long eventId) {
        return eventService.dislike(userId, eventId);
    }
}
