package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Закрытый API для работы с запросами текущего пользователя на участие в событиях
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ParticipationRequestsController {
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable long userId) {
        return null;
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto addParticipationRequest(
            @PathVariable long userId,
            @RequestParam long eventId
    ) {
        return null;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable long userId,
            @PathVariable long requestId
    ) {
        return null;
    }
}
