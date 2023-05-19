package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> getAllByUserId(Long userId);

    RequestDto create(Long userId, Long eventId);

    RequestDto update(Long userId, Long requestId);
}
