package ru.practicum.ewm.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.EventStatus;

import java.util.List;

/**
 * Изменение статуса запроса на участие в событии текущего пользователя
 */
@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private EventStatus status;
}
