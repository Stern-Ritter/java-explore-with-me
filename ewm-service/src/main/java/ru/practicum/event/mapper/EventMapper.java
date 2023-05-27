package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dto.CreateEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.AdminEventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.UserEventState;
import ru.practicum.event.view.EventFullView;
import ru.practicum.event.view.EventShortView;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.mapper.UserMapper;

import static ru.practicum.utils.Utils.coalesce;

public class EventMapper {
    public static Event toEvent(CreateEventDto eventDto) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        LocationDto locationDto = eventDto.getLocation();
        event.setLocation(locationDto != null ? LocationMapper.toLocation(locationDto) : null);
        event.setPaid(coalesce(eventDto.getPaid(), event.getPaid()));
        event.setParticipantLimit(coalesce(eventDto.getParticipantLimit(), event.getParticipantLimit()));
        event.setRequestModeration(coalesce(eventDto.getRequestModeration(), event.getRequestModeration()));
        event.setTitle(eventDto.getTitle());
        return event;
    }

    public static Event toEvent(UpdateEventUserRequest updateEventUserRequest) {
        Event event = new Event();
        event.setAnnotation(updateEventUserRequest.getAnnotation());
        event.setDescription(updateEventUserRequest.getDescription());
        event.setEventDate(updateEventUserRequest.getEventDate());
        LocationDto locationDto = updateEventUserRequest.getLocation();
        event.setLocation(locationDto != null ? LocationMapper.toLocation(locationDto) : null);
        event.setPaid(updateEventUserRequest.getPaid());
        event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        event.setState(parseEventState(updateEventUserRequest.getStateAction()));
        event.setTitle(updateEventUserRequest.getTitle());
        return event;
    }

    public static Event toEvent(UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = new Event();
        event.setAnnotation(updateEventAdminRequest.getAnnotation());
        event.setDescription(updateEventAdminRequest.getDescription());
        event.setEventDate((updateEventAdminRequest.getEventDate()));
        LocationDto locationDto = updateEventAdminRequest.getLocation();
        event.setLocation(locationDto != null ? LocationMapper.toLocation(locationDto) : null);
        event.setPaid(updateEventAdminRequest.getPaid());
        event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        event.setState(parseEventState(updateEventAdminRequest.getStateAction()));
        event.setTitle(updateEventAdminRequest.getTitle());
        return event;
    }

    public static EventFullDto toEventFullDto(EventFullView event) {
        EventFullDto eventDto = new EventFullDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()));
        eventDto.setConfirmedRequests(event.getConfirmedRequests());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setDescription(event.getDescription());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(event.getInitiator() == null ? null : UserMapper.toUserShortDto(event.getInitiator()));
        eventDto.setLocation(event.getLocation() == null ? null : LocationMapper.toLocationDto(event.getLocation()));
        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setTitle(event.getTitle());
        return eventDto;
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventDto = new EventFullDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()));
        eventDto.setConfirmedRequests(event.getRequests().stream().filter(e -> e.getStatus() == RequestStatus.CONFIRMED).count());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setDescription(event.getDescription());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(event.getInitiator() == null ? null : UserMapper.toUserShortDto(event.getInitiator()));
        eventDto.setLocation(event.getLocation() == null ? null : LocationMapper.toLocationDto(event.getLocation()));
        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setTitle(event.getTitle());
        return eventDto;
    }

    public static EventShortDto toEventShortDto(EventShortView event) {
        EventShortDto eventDto = new EventShortDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()));
        eventDto.setConfirmedRequests(event.getConfirmedRequests());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(event.getInitiator() == null ? null : UserMapper.toUserShortDto(event.getInitiator()));
        eventDto.setPaid(event.getPaid());
        eventDto.setTitle(event.getTitle());
        return eventDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventDto = new EventShortDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()));
        eventDto.setConfirmedRequests(event.getRequests().stream().filter(e -> e.getStatus() == RequestStatus.CONFIRMED).count());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(event.getInitiator() == null ? null : UserMapper.toUserShortDto(event.getInitiator()));
        eventDto.setPaid(event.getPaid());
        eventDto.setTitle(event.getTitle());
        return eventDto;
    }

    public static Event mergePatchedEvent(Event savedEvent, Event patchedEvent) {
        Event event = new Event();
        event.setId(savedEvent.getId());
        event.setCreatedOn(coalesce(patchedEvent.getCreatedOn(), savedEvent.getCreatedOn()));
        event.setPublishedOn(coalesce(patchedEvent.getPublishedOn(), savedEvent.getPublishedOn()));
        event.setEventDate(coalesce(patchedEvent.getEventDate(), savedEvent.getEventDate()));
        event.setTitle(coalesce(patchedEvent.getTitle(), savedEvent.getTitle()));
        event.setAnnotation(coalesce(patchedEvent.getAnnotation(), savedEvent.getAnnotation()));
        event.setDescription(coalesce(patchedEvent.getDescription(), savedEvent.getDescription()));
        event.setPaid(coalesce(patchedEvent.getPaid(), savedEvent.getPaid()));
        event.setRequestModeration(coalesce(patchedEvent.getRequestModeration(), savedEvent.getRequestModeration()));
        event.setParticipantLimit(coalesce(patchedEvent.getParticipantLimit(), savedEvent.getParticipantLimit()));
        event.setState(coalesce(patchedEvent.getState(), savedEvent.getState()));
        event.setCategory(coalesce(patchedEvent.getCategory(), savedEvent.getCategory()));
        event.setInitiator(coalesce(patchedEvent.getInitiator(), savedEvent.getInitiator()));
        event.setLocation(coalesce(patchedEvent.getLocation(), savedEvent.getLocation()));
        event.setRequests(coalesce(patchedEvent.getRequests(), savedEvent.getRequests()));
        return event;
    }

    private static EventState parseEventState(AdminEventState stateAction) {
        if (stateAction == AdminEventState.PUBLISH_EVENT) {
            return EventState.PUBLISHED;
        } else if (stateAction == AdminEventState.REJECT_EVENT) {
            return EventState.CANCELED;
        }
        return null;
    }

    private static EventState parseEventState(UserEventState stateAction) {
        if (stateAction == UserEventState.SEND_TO_REVIEW) {
            return EventState.PENDING;
        } else if (stateAction == UserEventState.CANCEL_REVIEW) {
            return EventState.CANCELED;
        }
        return null;
    }
}
