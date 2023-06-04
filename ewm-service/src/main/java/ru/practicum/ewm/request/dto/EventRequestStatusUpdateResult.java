package ru.practicum.ewm.request.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Результат подтверждения/отклонения заявок на участие в событии
 */
@Data
@Builder
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
