package ru.practicum.ewm.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.storage.EventAdminFilterParams;
import ru.practicum.ewm.event.storage.EventFilterParams;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventFullDto> getEvents(EventAdminFilterParams params, Pageable pageable);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    void updateEvent(Event updatedEvent);

    List<EventShortDto> getEvents(EventFilterParams params, Pageable pageable, HttpServletRequest request);

    EventFullDto getPublishedEventById(long eventId, HttpServletRequest request);

    EventFullDto getEventById(long eventId);

    List<EventShortDto> getUserEvents(long userId, Pageable pageable);

    EventFullDto addUserEvent(long userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(long userId, long eventId);

    EventFullDto updateUserEventById(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getEventsByIds(List<Long> eventIds);

    List<EventFullDto> getEventsByCategoryId(long catId);


}
