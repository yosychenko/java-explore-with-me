package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.EventSortType;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.stats.gateway.client.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Публичный API для работы с событиями
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam String text,
            @RequestParam List<Long> categories,
            @RequestParam boolean paid,
            @RequestParam LocalDateTime rangeStart,
            @RequestParam LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam EventSortType sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, PageRequest.of(from, size));
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }
}
