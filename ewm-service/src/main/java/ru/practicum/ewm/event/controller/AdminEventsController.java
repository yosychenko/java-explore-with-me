package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * API для работы с событиями
 */
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam List<Long> users,
            @RequestParam List<EventState> states,
            @RequestParam List<Long> categories,
            @RequestParam LocalDateTime rangeStart,
            @RequestParam LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest
    ) {
        return eventService.updateEvent(eventId, updateEventAdminRequest);
    }
}
