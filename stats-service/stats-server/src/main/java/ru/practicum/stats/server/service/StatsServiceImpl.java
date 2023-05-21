package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.EndpointHitStatsDto;
import ru.practicum.stats.server.mapper.EndpointHitMapper;
import ru.practicum.stats.server.storage.StatsStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsStorage statsStorage;

    @Override
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        statsStorage.save(EndpointHitMapper.fromDto(endpointHitDto));
    }

    @Override
    public List<EndpointHitStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        if (unique) {
            return statsStorage.findUniqueHits(uris, start, end);
        }
        return statsStorage.findAllHits(uris, start, end);
    }
}
