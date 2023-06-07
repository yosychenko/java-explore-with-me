package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * Закрытый API для работы с событиями
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserEventRequestsController {
    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.getUserEvents(userId, PageRequest.of(from, size));
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventFullDto addUserEvent(@PathVariable long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.addUserEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateUserEventById(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest
    ) {
        return eventService.updateUserEventById(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventParticipationRequests(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        return participationRequestService.getUserEventParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventParticipationRequestStatus(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        return participationRequestService.updateUserEventParticipationRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
