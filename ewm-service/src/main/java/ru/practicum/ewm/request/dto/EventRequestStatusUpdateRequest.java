package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.model.ParticipationRequestStatus;

import java.util.List;

/**
 * Изменение статуса запроса на участие в событии текущего пользователя
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private ParticipationRequestStatus status;
}
