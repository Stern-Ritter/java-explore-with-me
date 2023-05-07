package ru.practicum.stats.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT " +
            "new ru.practicum.stats.ViewStatsDto(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.app = :app " +
            "AND ((:uris) is null OR e.uri IN (:uris)) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStatsDto> getEndpointHitStats(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end,
                                           @Param("app") String app,
                                           @Param("uris") List<String> uris);

    @Query("SELECT " +
            "new ru.practicum.stats.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.app = :app " +
            "AND ((:uris) is null OR e.uri IN (:uris)) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStatsDto> getEndpointHitWithUniqueIpStats(@Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end,
                                                       @Param("app") String app,
                                                       @Param("uris") List<String> uris);
}
