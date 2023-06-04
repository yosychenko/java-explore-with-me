package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

/**
 * Закрытый API для работы с событиями
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserEventRequestsController {
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return null;
    }

    @PostMapping("/{userId}/events")
    public EventFullDto addUserEvent(@PathVariable long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return null;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable long userId, @PathVariable long eventId) {
        return null;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateUserEventById(@PathVariable long userId, @PathVariable long eventId) {
        return null;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventParticipationRequests(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        return null;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventParticipationRequestStatus(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        return null;
    }
}
