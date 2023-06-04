package ru.practicum.ewm.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.request.model.ParticipationRequestStatus;

import java.time.LocalDateTime;

/**
 * Заявка на участие в событии
 */
@Data
@Builder
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private ParticipationRequestStatus status;
}
