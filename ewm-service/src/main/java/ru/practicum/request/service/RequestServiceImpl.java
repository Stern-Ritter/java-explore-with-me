package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.utils.Templates.*;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<RequestDto> getAllByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto create(Long userId, Long eventId) {
        Request request = new Request();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        Long eventInitiatorId = event.getInitiator().getId();
        EventState eventState = event.getState();
        Integer participantLimit = event.getParticipantLimit();
        boolean isParticipantCountNotLimited = participantLimit == 0;
        boolean isModerationNotRequested = !event.getRequestModeration();
        Integer requestsCount = event.getRequests().size();

        if (Objects.equals(userId, eventInitiatorId)) {
            throw new ConflictException(REQUEST_INCORRECT_REQUESTER_EXCEPTION);
        }
        if (eventState != EventState.PUBLISHED) {
            throw new ConflictException(REQUEST_INCORRECT_EVENT_STATUS_EXCEPTION);
        }
        if (!isParticipantCountNotLimited && requestsCount.equals(participantLimit)) {
            throw new ConflictException(REQUEST_EVENT_LIMIT_OF_PARTICIPATION_EXCEPTION);
        }

        request.setRequester(user);
        request.setEvent(event);
        if (isParticipantCountNotLimited || isModerationNotRequested) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto update(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(REQUEST_NOT_EXISTS_TEMPLATE, requestId)));

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
