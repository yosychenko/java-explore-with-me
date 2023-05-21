package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.EndpointHitStatsDto;
import ru.practicum.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void createEndpointHit(@RequestBody EndpointHitDto endpointHitDto) {
        statsService.createEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<EndpointHitStatsDto> getStats(
            @RequestParam List<String> uris,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestBody boolean unique
    ) {
        return statsService.getStats(uris, start, end, unique);
    }
}
