package ru.practicum.stats.server.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.dto.EndpointHitStatsDto;
import ru.practicum.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsStorage extends JpaRepository<EndpointHit, Long> {
    @Query(
            "SELECT new ru.practicum.stats.dto.EndpointHitStatsDto(h.app, h.uri, COUNT(h.ip)) " +
                    "FROM EndpointHit h " +
                    "WHERE h.uri in :uris AND timestamp BETWEEN :start AND :end " +
                    "GROUP BY h.app, h.uri " +
                    "ORDER BY COUNT(h.ip) DESC"
    )
    List<EndpointHitStatsDto> findAllHitsByUri(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(
            "SELECT new ru.practicum.stats.dto.EndpointHitStatsDto(h.app, h.uri, COUNT(h.ip)) " +
                    "FROM EndpointHit h " +
                    "WHERE timestamp BETWEEN :start AND :end " +
                    "GROUP BY h.app, h.uri " +
                    "ORDER BY COUNT(h.ip) DESC"
    )
    List<EndpointHitStatsDto> findAllHits(LocalDateTime start, LocalDateTime end);

    @Query(
            "SELECT new ru.practicum.stats.dto.EndpointHitStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
                    "FROM EndpointHit h " +
                    "WHERE h.uri in :uris AND timestamp BETWEEN :start AND :end " +
                    "GROUP BY h.app, h.uri " +
                    "ORDER BY COUNT(h.ip) DESC"
    )
    List<EndpointHitStatsDto> findUniqueHitsByUri(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(
            "SELECT new ru.practicum.stats.dto.EndpointHitStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
                    "FROM EndpointHit h " +
                    "WHERE timestamp BETWEEN :start AND :end " +
                    "GROUP BY h.app, h.uri " +
                    "ORDER BY COUNT(h.ip) DESC"
    )
    List<EndpointHitStatsDto> findAllUniqueHits(LocalDateTime start, LocalDateTime end);

}
