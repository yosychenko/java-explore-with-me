package ru.practicum.stats.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.gateway.client.StatsClient;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatsGatewayController {

    private final StatsClient statsClient;

    @PostMapping("/hit")
    public void createEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        statsClient.createEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(
            @RequestParam List<String> uris,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestBody boolean unique
    ) {
        // TODO: Add start/end validation
        return statsClient.getStats(uris, start, end, unique);
    }
}
