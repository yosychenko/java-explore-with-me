package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.EventSortType;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.storage.EventStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventStorage eventStorage;

    @Override
    public List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            EventSortType sort,
            Pageable pageable
    ) {
        return null;
    }

    @Override
    public EventFullDto getEventById(Long id) {
        return null;
    }

    @Override
    public List<EventFullDto> getEventsByIds(List<Long> eventIds) {
        return eventStorage.findAllById(eventIds).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable
    ) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        return null;
    }
}
