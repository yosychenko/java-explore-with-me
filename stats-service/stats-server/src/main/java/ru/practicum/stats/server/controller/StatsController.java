package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.EndpointHitStatsDto;
import ru.practicum.stats.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Object> createEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        statsService.createEndpointHit(endpointHitDto);
        return new ResponseEntity<>(
                Map.of("message", "Информация сохранена."),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/stats")
    public List<EndpointHitStatsDto> getStats(
            @RequestParam(required = false) List<String> uris,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        if (start.isAfter(end)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Дата и время начала диапазона не должны быть позже даты и времени конца диапазона."
            );
        }

        return statsService.getStats(uris, start, end, unique);
    }
}
