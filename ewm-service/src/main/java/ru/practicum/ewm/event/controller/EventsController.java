package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.EventSortType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Публичный API для работы с событиями
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {
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
        return null;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id) {
        return null;
    }
}
