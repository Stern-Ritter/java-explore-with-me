package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.exceptions.BadRequestException;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.CreateEndpointHitDto;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.stats.utils.Templates.ENDPOINT_HIT_STATS_FILTER_DATES_VALIDATION_TEMPLATE;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<ViewStatsDto> getEndpointHitStats(LocalDateTime start, LocalDateTime end, String app,
                                                  List<String> uris, Boolean unique) {
        if (end.isBefore(start)) {
            throw new BadRequestException(ENDPOINT_HIT_STATS_FILTER_DATES_VALIDATION_TEMPLATE);
        }

        if (unique) {
            return statsRepository.getEndpointHitWithUniqueIpStats(start, end, app, uris);
        }
        return statsRepository.getEndpointHitStats(start, end, app, uris);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EndpointHitDto addEndpointHit(CreateEndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        return EndpointHitMapper.toEndpointHitDto(statsRepository.save(endpointHit));
    }
}
