package ru.practicum.ewm.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.EventSortType;
import ru.practicum.ewm.event.storage.EventAdminFilterParams;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            EventSortType sort,
            Pageable pageable
    );

    List<EventFullDto> getEvents(EventAdminFilterParams params, Pageable pageable);

    EventFullDto getEventById(long eventId);

    List<EventFullDto> getEventsByIds(List<Long> eventIds);

    List<EventFullDto> getEventsByCategoryId(long catId);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
