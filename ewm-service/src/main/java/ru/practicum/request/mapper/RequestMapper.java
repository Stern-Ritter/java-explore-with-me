package ru.practicum.request.mapper;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.view.RequestView;

public class RequestMapper {
    public static RequestDto toRequestDto(RequestView requestView) {
        RequestDto eventRequestDto = new RequestDto();
        eventRequestDto.setId(requestView.getId());
        eventRequestDto.setCreated(requestView.getCreated());
        eventRequestDto.setEvent(requestView.getEvent());
        eventRequestDto.setRequester(requestView.getRequester());
        eventRequestDto.setStatus(requestView.getStatus());
        return eventRequestDto;
    }

    public static RequestDto toRequestDto(Request request) {
        RequestDto eventRequestDto = new RequestDto();
        eventRequestDto.setId(request.getId());
        eventRequestDto.setCreated(request.getCreated());
        eventRequestDto.setEvent(request.getEvent() == null ? null : request.getEvent().getId());
        eventRequestDto.setRequester(request.getRequester() == null ? null : request.getRequester().getId());
        eventRequestDto.setStatus(request.getStatus());
        return eventRequestDto;
    }
}
