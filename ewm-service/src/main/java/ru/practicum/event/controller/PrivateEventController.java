package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.CreateEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getByEventIdAndUserId(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getByEventIdAndUserId(eventId, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllByUserId(@PathVariable Long userId,
                                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventService.getAllByUserId(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@RequestBody @Valid CreateEventDto eventDto,
                               @PathVariable Long userId) {
        return eventService.create(eventDto, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto update(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                               @PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.update(updateEventUserRequest, userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getEventRequestsByEventIdAndUserId(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        return eventService.getEventRequestsByEventIdAndUserId(eventId, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateEventRequestsByEventIdAndUserId(
            @RequestBody @Valid EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto,
            @PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.updateEventRequestsByEventIdAndUserId(eventRequestStatusUpdateRequestDto, userId, eventId);
    }
}
