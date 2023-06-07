package ru.practicum.ewm.stats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.StatsServiceException;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.EndpointHitStatsDto;
import ru.practicum.stats.gateway.client.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsClient statsClient;
    private final DateTimeFormatter dateTimeFormatter;
    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    public StatsServiceImpl(
            @Value("${spring.application.name}") String appName,
            @Value("${format.pattern.datetime}") String dateTimeFormat,
            StatsClient statsClient
    ) {
        this.appName = appName;
        this.statsClient = statsClient;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
    }


    @Override
    public void createEndpointHit(String ipAddr, String requestUri) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(appName)
                .ip(ipAddr)
                .uri(requestUri)
                .timestamp(LocalDateTime.now())
                .build();
        ResponseEntity<Object> response = statsClient.createEndpointHit(endpointHitDto);
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new StatsServiceException("Не удалось сохранить данные в сервис статистики.");
        }
    }

    @Override
    public List<EndpointHitStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        if (start == null || end == null) {
            start = LocalDateTime.of(2000, 1, 1, 0, 0);
            end = LocalDateTime.of(9999, 1, 1, 0, 0);
        }

        ResponseEntity<Object> response = statsClient.getStats(
                uris,
                start.format(dateTimeFormatter),
                end.format(dateTimeFormatter),
                unique
        );
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new StatsServiceException("Не удалось получить данные из сервиса статистики.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> stats = (List<Object>) response.getBody();
        return stats.stream()
                .map(object -> objectMapper.convertValue(object, EndpointHitStatsDto.class))
                .collect(Collectors.toList());
    }
}
