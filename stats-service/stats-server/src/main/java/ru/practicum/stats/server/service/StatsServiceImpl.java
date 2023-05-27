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
            if (!uris.isEmpty()) {
                return statsStorage.findUniqueHitsByUri(uris, start, end);
            } else {
                return statsStorage.findAllUniqueHits(start, end);
            }

        } else {
            if (!uris.isEmpty()) {
                return statsStorage.findAllHitsByUri(uris, start, end);
            } else {
                return statsStorage.findAllHits(start, end);
            }
        }
    }
}
