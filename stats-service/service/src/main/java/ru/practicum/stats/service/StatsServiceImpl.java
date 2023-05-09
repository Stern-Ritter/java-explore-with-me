package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.stats.CreateEndpointHitDto;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.storage.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<ViewStatsDto> getEndpointHitStats(LocalDateTime start, LocalDateTime end, String app,
                                                  List<String> uris, Boolean unique) {
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
