package ru.practicum.ewm.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.EventSortType;
import ru.practicum.ewm.event.model.EventState;

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

    EventFullDto getEventById(Long id);

    List<EventFullDto> getEventsByIds(List<Long> eventIds);

    List<EventFullDto> getEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable
    );

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
