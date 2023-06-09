package ru.practicum.ewm.stats.service;

import ru.practicum.stats.dto.EndpointHitStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void createEndpointHit(String ipAddr, String requestUri);

    List<EndpointHitStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique);
}
