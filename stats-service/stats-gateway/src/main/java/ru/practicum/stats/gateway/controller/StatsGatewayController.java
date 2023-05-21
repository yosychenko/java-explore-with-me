package ru.practicum.stats.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.gateway.client.StatsClient;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsGatewayController {

    private final StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> createEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        return statsClient.createEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(
            @RequestParam(required = false, defaultValue = "") List<String> uris,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start ,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    ) {
        // TODO: Add start/end validation
        return statsClient.getStats(uris, start, end, unique);
    }
}
