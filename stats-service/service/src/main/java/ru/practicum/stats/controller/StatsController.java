package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats.CreateEndpointHitDto;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.service.StatsService;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.utils.Templates.GET_STATS_TEMPLATE;
import static ru.practicum.utils.Templates.POST_ENDPOINT_HIT;

@RestController
@RequestMapping("/")
@Validated
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final String defaultAppName = "ewm-main-service";
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getEndpointHitStats(@RequestParam(name = "start") String encodedStart,
                                                  @RequestParam(name = "end") String encodedEnd,
                                                  @RequestParam(name = "app", defaultValue = defaultAppName) String app,
                                                  @RequestParam(name = "uris", required = false) List<String> uris,
                                                  @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        LocalDateTime start = parseDate(decode(encodedStart));
        LocalDateTime end = parseDate(decode(encodedEnd));
        log.info(String.format(GET_STATS_TEMPLATE, start, end, uris, unique));
        return statsService.getEndpointHitStats(start, end, app, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto addEndpointHit(@RequestBody @Valid CreateEndpointHitDto endpointHitDto) {
        log.info(String.format(POST_ENDPOINT_HIT, endpointHitDto));
        return statsService.addEndpointHit(endpointHitDto);
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
