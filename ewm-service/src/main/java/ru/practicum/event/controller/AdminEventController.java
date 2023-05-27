package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Utils.decode;
import static ru.practicum.utils.Utils.parseDate;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllWithAdminFilters(@RequestParam(name = "users", required = false) List<Long> users,
                                                     @RequestParam(name = "states", required = false) List<EventState> states,
                                                     @RequestParam(name = "categories", required = false) List<Long> categories,
                                                     @RequestParam(name = "rangeStart", required = false) String encodedRangeStart,
                                                     @RequestParam(name = "rangeEnd", required = false) String encodedRangeEnd,
                                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        LocalDateTime rangeStart = encodedRangeStart != null ? parseDate(decode(encodedRangeStart)) : null;
        LocalDateTime rangeEnd = encodedRangeEnd != null ? parseDate(decode(encodedRangeEnd)) : null;
        return eventService.getAllWithAdminFilters(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest,
                               @PathVariable Long eventId) {
        return eventService.update(updateEventAdminRequest, eventId);
    }
}
