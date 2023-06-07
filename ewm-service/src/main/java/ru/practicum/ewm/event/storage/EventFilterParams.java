package ru.practicum.ewm.event.storage;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.event.model.EventSortType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventFilterParams {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
    private EventSortType sort;
}
