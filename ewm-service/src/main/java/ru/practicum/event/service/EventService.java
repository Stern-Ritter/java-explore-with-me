package ru.practicum.event.service;

import ru.practicum.event.dto.CreateEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateResultDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto getById(Long eventId);

    List<EventFullDto> getAllWithFilters(String text, List<Long> categories, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                         EventSort sort, Integer offset, Integer limit);

    List<EventFullDto> getAllWithAdminFilters(List<Long> users, List<EventState> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              Integer offset, Integer limit);

    EventFullDto getByEventIdAndUserId(Long eventId, Long userId);

    List<EventShortDto> getAllByUserId(Long userId, Integer offset, Integer limit);

    List<RequestDto> getEventRequestsByEventIdAndUserId(Long eventId, Long userId);

    EventFullDto create(CreateEventDto eventDto, Long userId);

    EventFullDto update(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

    EventFullDto update(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId);

    EventRequestStatusUpdateResultDto updateEventRequestsByEventIdAndUserId(
            EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto, Long userId, Long eventId);

    EventFullDto like(Long userId, Long eventId);

    EventFullDto dislike(Long userId, Long eventId);
}
