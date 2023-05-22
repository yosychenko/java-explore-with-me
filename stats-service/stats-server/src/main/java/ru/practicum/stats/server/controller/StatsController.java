package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.EndpointHitStatsDto;
import ru.practicum.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Object> createEndpointHit(@RequestBody EndpointHitDto endpointHitDto) {
        statsService.createEndpointHit(endpointHitDto);
        return new ResponseEntity<>(
                Map.of("message", "Информация сохранена."),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/stats")
    public List<EndpointHitStatsDto> getStats(
            @RequestParam List<String> uris,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam boolean unique
    ) {
        return statsService.getStats(uris, start, end, unique);
    }
}
