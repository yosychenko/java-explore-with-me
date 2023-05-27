package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.EndpointHitStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void createEndpointHit(EndpointHitDto endpointHitDto);

    List<EndpointHitStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique);
}
