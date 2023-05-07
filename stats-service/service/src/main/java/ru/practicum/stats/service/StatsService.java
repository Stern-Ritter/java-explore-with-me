package ru.practicum.stats.service;

import ru.practicum.stats.CreateEndpointHitDto;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStatsDto> getEndpointHitStats(LocalDateTime start, LocalDateTime end, String app,
                                           List<String> uris, Boolean unique);

    EndpointHitDto addEndpointHit(CreateEndpointHitDto endpointHitDto);
}
