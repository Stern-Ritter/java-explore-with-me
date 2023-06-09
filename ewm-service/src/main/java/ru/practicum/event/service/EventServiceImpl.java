package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.CreateEventDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.AdminEventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.view.EventFullView;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.model.UpdateRequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.view.RequestView;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.utils.Templates.*;
import static ru.practicum.utils.Utils.APPLICATION_NAME;
import static ru.practicum.utils.Utils.calculateFirstPageNumber;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final int minEventDateOffset = 2;
    private final int endpointHitMonthsOffset = 3;
    private final boolean countUniqueIpAddresses = true;

    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final Sort.TypedSort<EventFullView> eventSort = Sort.sort(EventFullView.class);

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public EventFullDto getById(Long eventId) {
        EventFullView savedEvent = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        EventFullDto event = EventMapper.toEventFullDto(savedEvent);
        addViewsCount(event, countUniqueIpAddresses);
        return event;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<EventFullDto> getAllWithFilters(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                EventSort sort, Integer offset, Integer limit) {
        validateEventFilterDates(rangeStart, rangeEnd);

        Sort sortByIdDesc = eventSort.by(EventFullView::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdDesc);
        List<EventFullDto> events = eventRepository.findAllWithFilters(text, categories, paid, rangeStart, rangeEnd, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

        if (sort == EventSort.EVENT_DATE) {
            events = events.stream().sorted(Comparator.comparing(EventFullDto::getEventDate)).collect(Collectors.toList());
        } else if (sort == EventSort.VIEWS) {
            events = events.stream().sorted(Comparator.comparingLong(EventFullDto::getViews)).collect(Collectors.toList());
        } else if (sort == EventSort.EVENT_RATING) {
            events = events.stream().sorted(Comparator.comparingInt(EventFullDto::getRating).reversed()).collect(Collectors.toList());
        } else if (sort == EventSort.EVENT_INITIATOR_RATING) {
            events = events.stream().sorted(Comparator.comparingLong(e -> -e.getInitiator().getRating())).collect(Collectors.toList());
        }

        if (events.size() != 0) {
            addViewsCount(events, countUniqueIpAddresses);
        }
        return events;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<EventFullDto> getAllWithAdminFilters(List<Long> users, List<EventState> states,
                                                     List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Integer offset, Integer limit) {
        validateEventFilterDates(rangeStart, rangeEnd);

        Sort sortByIdDesc = eventSort.by(EventFullView::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdDesc);
        List<EventFullDto> events = eventRepository.findAllWithAdminFilters(users, states, categories, rangeStart, rangeEnd, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

        if (events.size() != 0) {
            addViewsCount(events, countUniqueIpAddresses);
        }
        return events;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public EventFullDto getByEventIdAndUserId(Long eventId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        EventFullView savedEvent = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        EventFullDto event = EventMapper.toEventFullDto(savedEvent);
        addViewsCount(event, countUniqueIpAddresses);
        return event;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<EventShortDto> getAllByUserId(Long userId, Integer offset, Integer limit) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        Sort sortByIdDesc = eventSort.by(EventFullView::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdDesc);
        List<EventShortDto> events = eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        if (events.size() != 0) {
            addViewsCount(events, countUniqueIpAddresses);
        }
        return events;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<RequestDto> getEventRequestsByEventIdAndUserId(Long eventId, Long userId) {
        List<RequestView> eventRequests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        return eventRequests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventFullDto create(CreateEventDto eventDto, Long userId) {
        validateEventDate(eventDto.getEventDate());

        Long categoryId = eventDto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_EXISTS_TEMPLATE, categoryId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        Event createdItem = EventMapper.toEvent(eventDto);
        createdItem.setCategory(category);
        createdItem.setInitiator(user);
        createdItem.setState(EventState.PENDING);

        EventFullDto event = EventMapper.toEventFullDto(eventRepository.save(createdItem));
        addViewsCount(event, countUniqueIpAddresses);
        return event;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventFullDto update(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event savedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        Event patchedItem = EventMapper.toEvent(updateEventAdminRequest);

        Long categoryId = updateEventAdminRequest.getCategory();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_EXISTS_TEMPLATE, categoryId)));
            patchedItem.setCategory(category);
        }

        validateUpdateByAdminEventState(savedEvent.getState(), patchedItem.getState());
        Event mergedEvent = EventMapper.mergePatchedEvent(savedEvent, patchedItem);
        validateEventDate(mergedEvent.getEventDate());

        if (updateEventAdminRequest.getStateAction() == AdminEventState.PUBLISH_EVENT) {
            mergedEvent.setPublishedOn(LocalDateTime.now());
        }

        EventFullDto event = EventMapper.toEventFullDto(eventRepository.save(mergedEvent));
        addViewsCount(event, countUniqueIpAddresses);
        return event;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventFullDto update(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {
        Event savedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        Event patchedItem = EventMapper.toEvent(updateEventUserRequest);

        Long categoryId = updateEventUserRequest.getCategory();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_EXISTS_TEMPLATE, categoryId)));
            patchedItem.setCategory(category);
        }

        validateUpdateByUserEventState(savedEvent.getState());
        Event mergedEvent = EventMapper.mergePatchedEvent(savedEvent, patchedItem);
        validateEventDate(mergedEvent.getEventDate());

        EventFullDto event = EventMapper.toEventFullDto(eventRepository.save(mergedEvent));
        addViewsCount(event, countUniqueIpAddresses);
        return event;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventRequestStatusUpdateResultDto updateEventRequestsByEventIdAndUserId(
            EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto, Long userId, Long eventId) {
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));
        List<Long> requestIds = eventRequestStatusUpdateRequestDto.getRequestIds();
        List<Request> requests = event.getRequests().stream()
                .filter(request -> requestIds.contains(request.getId()))
                .collect(Collectors.toList());

        UpdateRequestStatus updateRequestStatus = eventRequestStatusUpdateRequestDto.getStatus();

        if (updateRequestStatus == UpdateRequestStatus.CONFIRMED) {
            int currentRequestsCount = requests.size();
            int participantLimit = event.getParticipantLimit();
            boolean isParticipantCountNotLimited = participantLimit == 0;
            boolean isModerationNotRequested = !event.getRequestModeration();
            int newRequestsConfirmedCount = 0;

            if (isParticipantCountNotLimited || isModerationNotRequested) {
                for (Request request : requests) {
                    if (request.getStatus() == RequestStatus.PENDING) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        confirmedRequests.add(request);
                    } else {
                        throw new ConflictException(String.format(EVENT_REQUEST_CONFIRMATION_INCORRECT_STATUS_EXCEPTION));
                    }
                }
            } else {
                for (Request request : requests) {
                    if (currentRequestsCount + newRequestsConfirmedCount >= participantLimit) {
                        throw new ConflictException(EVENT_REQUEST_PARTICIPANT_LIMIT_EXCEPTION);
                    } else if (request.getStatus() == RequestStatus.PENDING) {
                        newRequestsConfirmedCount += 1;
                        request.setStatus(RequestStatus.CONFIRMED);
                        confirmedRequests.add(request);
                    } else {
                        throw new ConflictException(String.format(EVENT_REQUEST_CONFIRMATION_INCORRECT_STATUS_EXCEPTION));
                    }
                }
            }


        } else if (updateRequestStatus == UpdateRequestStatus.REJECTED) {
            for (Request request : requests) {
                if (request.getStatus() == RequestStatus.PENDING) {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(request);
                } else {
                    throw new ConflictException(String.format(EVENT_REQUEST_CONFIRMATION_INCORRECT_STATUS_EXCEPTION));
                }
            }
        }

        eventRepository.save(event);

        EventRequestStatusUpdateResultDto eventRequestStatusUpdateResultDto = new EventRequestStatusUpdateResultDto();
        List<RequestDto> confirmedRequestsDto = confirmedRequests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        List<RequestDto> rejectedRequestsDto = rejectedRequests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        eventRequestStatusUpdateResultDto.setConfirmedRequests(confirmedRequestsDto);
        eventRequestStatusUpdateResultDto.setRejectedRequests(rejectedRequestsDto);

        return eventRequestStatusUpdateResultDto;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventFullDto like(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        Set<User> likes = event.getLikes();
        Set<User> dislikes = event.getDislikes();
        boolean isAlreadyLikedByUser = likes.stream().anyMatch(u -> Objects.equals(u.getId(), userId));

        if (isAlreadyLikedByUser) {
            throw new ForbiddenException(EVENT_LIKED_BY_USER_MORE_THAN_ONCE_EXCEPTION);
        }

        Set<Long> confirmedRequestsUserIds = event.getRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.CONFIRMED)
                .map(request -> request.getRequester().getId())
                .collect(Collectors.toSet());
        Long eventInitiatorId = event.getInitiator().getId();

        boolean isNonEventParticipant = !Objects.equals(eventInitiatorId, userId) && !confirmedRequestsUserIds.contains(userId);
        if (isNonEventParticipant) {
            throw new ForbiddenException(EVENT_LIKED_BY_NON_PARTICIPANT_EXCEPTION);
        }

        dislikes = dislikes.stream().filter(u -> !Objects.equals(u.getId(), userId)).collect(Collectors.toSet());
        likes.add(user);

        event.setDislikes(dislikes);
        event.setLikes(likes);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventFullDto dislike(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        Set<User> dislikes = event.getDislikes();
        Set<User> likes = event.getLikes();
        boolean isAlreadyDislikedByUser = dislikes.stream().anyMatch(u -> Objects.equals(u.getId(), userId));

        if (isAlreadyDislikedByUser) {
            throw new ForbiddenException(EVENT_DISLIKED_BY_USER_MORE_THAN_ONCE_EXCEPTION);
        }

        Set<Long> confirmedRequestsUserIds = event.getRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.CONFIRMED)
                .map(request -> request.getRequester().getId())
                .collect(Collectors.toSet());
        Long eventInitiatorId = event.getInitiator().getId();

        boolean isNonEventParticipant = !Objects.equals(eventInitiatorId, userId) && !confirmedRequestsUserIds.contains(userId);
        if (isNonEventParticipant) {
            throw new ForbiddenException(EVENT_DISLIKED_BY_NON_PARTICIPANT_EXCEPTION);
        }

        likes = likes.stream().filter(u -> !Objects.equals(u.getId(), userId)).collect(Collectors.toSet());
        dislikes.add(user);

        event.setLikes(likes);
        event.setDislikes(dislikes);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime currentDate = LocalDateTime.now();
        if (eventDate.isBefore(currentDate.plusHours(minEventDateOffset))) {
            throw new BadRequestException(String.format(EVENT_EVENT_DATE_VALIDATION_EXCEPTION, minEventDateOffset));
        }
    }

    private boolean isNotNull(Object object) {
        return object != null;
    }

    private void validateEventFilterDates(LocalDateTime start, LocalDateTime end) {
        if (isNotNull(start) && isNotNull(end) && end.isBefore(start)) {
            throw new BadRequestException(EVENT_FILTER_DATES_VALIDATION_TEMPLATE);
        }
    }

    private void validateUpdateByAdminEventState(EventState savedEventState, EventState patchedEventState) {
        if (patchedEventState == EventState.PUBLISHED && savedEventState != EventState.PENDING) {
            throw new ForbiddenException(String.format(EVENT_PUBLISH_STATE_VALIDATION_EXCEPTION,
                    savedEventState.name()));
        }

        if (patchedEventState == EventState.CANCELED && savedEventState != EventState.PENDING) {
            throw new ForbiddenException(String.format(EVENT_CANCEL_STATE_VALIDATION_EXCEPTION,
                    savedEventState.name()));
        }
    }

    public void validateUpdateByUserEventState(EventState eventState) {
        if (eventState != EventState.PENDING && eventState != EventState.CANCELED) {
            throw new ForbiddenException(String.format(EVENT_PUBLISH_STATE_VALIDATION_EXCEPTION,
                    eventState.name()));
        }
    }

    private void addViewsCount(EventDto event, boolean unique) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusYears(endpointHitMonthsOffset);
        List<String> uris = List.of(String.format("/events/%d", event.getId()));
        List<ViewStatsDto> viewStats = statsClient.getEndpointHitStats(start, end, APPLICATION_NAME, uris, unique);
        event.setViews(viewStats.stream().map(ViewStatsDto::getHits).findFirst().orElseGet(() -> 0L));
    }

    private void addViewsCount(List<? extends EventDto> events, boolean unique) {
        Map<Long, EventDto> eventMap = new HashMap<>();
        events.forEach((event) -> eventMap.put(event.getId(), event));
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusYears(endpointHitMonthsOffset);
        List<String> uris = events.stream()
                .map(EventDto::getId).map((id) -> String.format("/events/%d", id))
                .collect(Collectors.toList());
        List<ViewStatsDto> viewStats = statsClient.getEndpointHitStats(start, end, APPLICATION_NAME, uris, unique);
        viewStats
                .stream()
                .filter((viewStat) -> !viewStat.getUri().equals("/events"))
                .forEach((viewStat) -> {
                    EventDto event = eventMap.get(Long.parseLong(viewStat.getUri().replace("/events/", "")));
                    event.setViews(viewStat.getHits());
                });
    }
}
