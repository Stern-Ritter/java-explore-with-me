package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.AdminEventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.view.EventFullView;
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
    private final static int MIN_EVENT_DATE_OFFSET = 2;
    private final static int ENDPOINT_HIT_MONTHS_OFFSET = 3;
    private final static boolean COUNT_UNIQUE_IP_ADDRESSES = true;

    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final Sort.TypedSort<EventFullView> eventSort = Sort.sort(EventFullView.class);

    @Override
    public EventFullDto getById(Long eventId) {
        EventFullView savedEvent = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        EventFullDto event = EventMapper.toEventFullDto(savedEvent);
        addViewsCount(event, COUNT_UNIQUE_IP_ADDRESSES);
        return event;
    }

    @Override
    public List<EventFullDto> getAllWithFilters(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                EventSort sort, Integer offset, Integer limit) {
        Sort sortByIdDesc = eventSort.by(EventFullView::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdDesc);
        List<EventFullDto> events = eventRepository.findAllWithFilters(text, categories, paid, rangeStart, rangeEnd, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

        if (sort == EventSort.EVENT_DATE) {
            events = events.stream().sorted(Comparator.comparing(EventFullDto::getEventDate)).collect(Collectors.toList());
        } else if (sort == EventSort.VIEWS) {
            events = events.stream().sorted(Comparator.comparingLong(EventFullDto::getViews)).collect(Collectors.toList());
        }

        addViewsCount(events, COUNT_UNIQUE_IP_ADDRESSES);
        return events;
    }

    @Override
    public List<EventFullDto> getAllWithAdminFilters(List<Long> users, List<EventState> states,
                                                     List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Integer offset, Integer limit) {
        Sort sortByIdDesc = eventSort.by(EventFullView::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdDesc);
        List<EventFullDto> events = eventRepository.findAllWithAdminFilters(users, states, categories, rangeStart, rangeEnd, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

        addViewsCount(events, COUNT_UNIQUE_IP_ADDRESSES);
        return events;
    }

    @Override
    public EventFullDto getByEventIdAndUserId(Long eventId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        EventFullView savedEvent = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_EXISTS_TEMPLATE, eventId)));

        EventFullDto event = EventMapper.toEventFullDto(savedEvent);
        addViewsCount(event, COUNT_UNIQUE_IP_ADDRESSES);
        return event;
    }

    @Override
    public List<EventShortDto> getAllByUserId(Long userId, Integer offset, Integer limit) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        Sort sortByIdDesc = eventSort.by(EventFullView::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdDesc);
        List<EventShortDto> events = eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        addViewsCount(events, COUNT_UNIQUE_IP_ADDRESSES);
        return events;
    }

    @Override
    public List<RequestDto> getEventRequestsByEventIdAndUserId(Long eventId, Long userId) {
        List<RequestView> eventRequests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));

        return eventRequests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
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
        addViewsCount(event, COUNT_UNIQUE_IP_ADDRESSES);
        return event;
    }

    @Override
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
        addViewsCount(event, COUNT_UNIQUE_IP_ADDRESSES);
        return event;
    }

    @Override
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
        addViewsCount(event, COUNT_UNIQUE_IP_ADDRESSES);
        return event;
    }

    @Override
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

    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime currentDate = LocalDateTime.now();
        if (eventDate.isBefore(currentDate.plusHours(MIN_EVENT_DATE_OFFSET))) {
            throw new ForbiddenException(String.format(EVENT_EVENT_DATE_VALIDATION_EXCEPTION, MIN_EVENT_DATE_OFFSET));
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
        LocalDateTime start = end.minusYears(ENDPOINT_HIT_MONTHS_OFFSET);
        List<String> uris = List.of(String.format("/events/%d", event.getId()));
        List<ViewStatsDto> viewStats = statsClient.getEndpointHitStats(start, end, APPLICATION_NAME, uris, unique);
        event.setViews(viewStats.stream().map(ViewStatsDto::getHits).findFirst().orElseGet(() -> 0L));
    }

    private void addViewsCount(List<? extends EventDto> events, boolean unique) {
        Map<Long, EventDto> eventMap = new HashMap<>();
        events.forEach((event) -> eventMap.put(event.getId(), event));
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusYears(ENDPOINT_HIT_MONTHS_OFFSET);
        List<String> uris = events.stream()
                .map(EventDto::getId).map((id) -> String.format("/events/%d", id))
                .collect(Collectors.toList());
        List<ViewStatsDto> viewStats = statsClient.getEndpointHitStats(start, end, APPLICATION_NAME, uris, unique);
        viewStats
                .forEach((viewStat) -> {
                    EventDto event = eventMap.get(Long.parseLong(viewStat.getUri().replace("/events/", "")));
                    event.setViews(viewStat.getHits());
                });
    }
}
