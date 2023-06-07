package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import java.util.List;

/**
 * Закрытый API для работы с запросами текущего пользователя на участие в событиях
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ParticipationRequestsController {

    private final ParticipationRequestService participationRequestService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable long userId) {
        return participationRequestService.getParticipationRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(
            @PathVariable long userId,
            @RequestParam long eventId
    ) {
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable long userId,
            @PathVariable long requestId
    ) {
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }
}
