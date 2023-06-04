package ru.practicum.ewm.event.storage;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventAdminFilterParams {
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
