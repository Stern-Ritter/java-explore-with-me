package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.service.EventService;
import ru.practicum.stats.CreateEndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Utils.APPLICATION_NAME;
import static ru.practicum.utils.Utils.decode;
import static ru.practicum.utils.Utils.parseDate;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final StatsClient statsClient;
    private final EventService eventService;

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable Long id, HttpServletRequest request) {
        hitEndpoint(request.getRequestURI(), request.getRemoteAddr());
        return eventService.getById(id);
    }

    @GetMapping
    public List<EventFullDto> getAllWithFilters(@RequestParam(name = "text", required = false) String text,
                                                @RequestParam(name = "categories", required = false) List<Long> categories,
                                                @RequestParam(name = "paid", required = false) Boolean paid,
                                                @RequestParam(name = "rangeStart", required = false) String encodedRangeStart,
                                                @RequestParam(name = "rangeEnd", required = false) String encodedRangeEnd,
                                                @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(name = "sort", required = false) EventSort sort,
                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        hitEndpoint(request.getRequestURI(), request.getRemoteAddr());
        LocalDateTime rangeStart = encodedRangeStart != null ? parseDate(decode(encodedRangeStart)) : null;
        LocalDateTime rangeEnd = encodedRangeEnd != null ? parseDate(decode(encodedRangeEnd)) : null;
        return eventService.getAllWithFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    private void hitEndpoint(String uri, String ip) {
        CreateEndpointHitDto endpointHitDto = new CreateEndpointHitDto(APPLICATION_NAME, uri, ip, LocalDateTime.now());
        statsClient.addEndpointHit(endpointHitDto);
    }
}
