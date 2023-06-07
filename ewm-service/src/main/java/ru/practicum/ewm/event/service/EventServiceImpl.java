package ru.practicum.ewm.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoriesService;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventStateAction;
import ru.practicum.ewm.event.storage.EventAdminFilterParams;
import ru.practicum.ewm.event.storage.EventFilterParams;
import ru.practicum.ewm.event.storage.EventFilterSpecifications;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.IncorrectEventDate;
import ru.practicum.ewm.exception.IncorrectEventStateAction;
import ru.practicum.ewm.stats.service.StatsService;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.stats.dto.EndpointHitStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventStorage eventStorage;
    private final UserService userService;
    private final StatsService statsService;
    private final CategoriesService categoriesService;


    @Autowired
    public EventServiceImpl(
            EventStorage eventStorage,
            UserService userService,
            StatsService statsService,
            @Lazy CategoriesService categoriesService
    ) {
        this.eventStorage = eventStorage;
        this.userService = userService;
        this.statsService = statsService;
        this.categoriesService = categoriesService;
    }

    @Override
    public List<EventFullDto> getEvents(EventAdminFilterParams params, Pageable pageable) {
        Specification<Event> specification = EventFilterSpecifications.getEventsAdminFilterSpecification(params);
        List<Event> events = eventStorage.findAll(specification, pageable).getContent();

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event eventToUpdate = EventMapper.fromEventFullDto(getEventById(eventId));
        LocalDateTime publishedOn = LocalDateTime.now();

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(EventStateAction.PUBLISH_EVENT) &&
                    !eventToUpdate.getState().equals(EventState.PENDING)) {
                throw new IncorrectEventStateAction("Событие можно публиковать, только если оно в состоянии ожидания публикации.");
            }

            if (updateEventAdminRequest.getStateAction().equals(EventStateAction.PUBLISH_EVENT) &&
                    ChronoUnit.HOURS.between(publishedOn, eventToUpdate.getEventDate()) < 1) {
                throw new IncorrectEventDate("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }

            if (updateEventAdminRequest.getStateAction().equals(EventStateAction.REJECT_EVENT) &&
                    eventToUpdate.getState().equals(EventState.PUBLISHED)) {
                throw new IncorrectEventStateAction("Событие можно отклонить, только если оно еще не опубликовано.");
            }

            if (updateEventAdminRequest.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                eventToUpdate.setState(EventState.PUBLISHED);
                eventToUpdate.setPublishedOn(LocalDateTime.now());
            }

            if (updateEventAdminRequest.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                eventToUpdate.setState(EventState.PENDING);
            }

            if (updateEventAdminRequest.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
                eventToUpdate.setState(EventState.CANCELED);
            }
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            eventToUpdate.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getCategory() != null) {
            Category category = CategoryMapper.fromCategoryDto(categoriesService.getCategoryById(updateEventAdminRequest.getCategory()));
            eventToUpdate.setCategory(category);
        }

        if (updateEventAdminRequest.getDescription() != null) {
            eventToUpdate.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            if (ChronoUnit.HOURS.between(LocalDateTime.now(), updateEventAdminRequest.getEventDate()) < 2) {
                throw new IncorrectEventDate("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.");
            }
            eventToUpdate.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (updateEventAdminRequest.getLocation() != null) {
            eventToUpdate.setLocation(updateEventAdminRequest.getLocation());
        }

        if (updateEventAdminRequest.getPaid() != null) {
            eventToUpdate.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            eventToUpdate.setTitle(updateEventAdminRequest.getTitle());
        }

        return EventMapper.toEventFullDto(eventStorage.save(eventToUpdate));

    }

    @Override
    public Event updateEvent(Event updatedEvent) {
        return eventStorage.save(updatedEvent);
    }

    @Override
    public List<EventShortDto> getEvents(EventFilterParams params, Pageable pageable, HttpServletRequest request) {
        Specification<Event> specification = EventFilterSpecifications.getEventsFilterSpecification(params);
        List<Event> events = eventStorage.findAll(specification, pageable).getContent();

        statsService.createEndpointHit(request.getRemoteAddr(), request.getRequestURI());

        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublishedEventById(long eventId, HttpServletRequest request) throws JsonProcessingException {
        Optional<Event> optionalEvent = eventStorage.findEventByIdAndState(eventId, EventState.PUBLISHED);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();

            statsService.createEndpointHit(request.getRemoteAddr(), request.getRequestURI());
            List<EndpointHitStatsDto> stats = statsService.getStats(
                    List.of(request.getRequestURI()),
                    null,
                    null,
                    true
            );

            if (!event.getViews().equals(stats.get(0).getHits())) {
                event.setViews(stats.get(0).getHits());
                eventStorage.save(event);
            }

            return EventMapper.toEventFullDto(event);
        } else {
            throw new EntityNotFoundException(String.format("Событие с ID=%s не найдено", eventId));
        }
    }

    @Override
    public EventFullDto getEventById(long eventId) {
        Optional<Event> event = eventStorage.findById(eventId);
        if (event.isPresent()) {
            return EventMapper.toEventFullDto(event.get());
        } else {
            throw new EntityNotFoundException(String.format("Событие с ID=%s не найдено", eventId));
        }
    }

    @Override
    @Transactional
    public List<EventShortDto> getUserEvents(long userId, Pageable pageable) {
        User initiator = UserMapper.fromUserDto(userService.getUserById(userId));
        List<Event> events = eventStorage.findAllByInitiator(initiator, pageable);

        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto addUserEvent(long userId, NewEventDto newEventDto) {
        if (ChronoUnit.HOURS.between(LocalDateTime.now(), newEventDto.getEventDate()) < 2) {
            throw new IncorrectEventDate("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.");
        }

        User initiator = UserMapper.fromUserDto(userService.getUserById(userId));
        Category category = CategoryMapper.fromCategoryDto(
                categoriesService.getCategoryById(newEventDto.getCategory())
        );

        Event newEvent = EventMapper.fromNewEventDto(newEventDto, category);
        newEvent.setInitiator(initiator);

        return EventMapper.toEventFullDto(eventStorage.save(newEvent));
    }

    @Override
    public EventFullDto getUserEventById(long userId, long eventId) {
        Optional<Event> event = eventStorage.findEventByInitiatorIdAndId(userId, eventId);
        if (event.isPresent()) {
            return EventMapper.toEventFullDto(event.get());
        } else {
            throw new EntityNotFoundException(String.format("Событие с ID=%s и инициатором с ID=%s не найдено.", eventId, userId));
        }
    }

    @Override
    public EventFullDto updateUserEventById(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event eventToUpdate = EventMapper.fromEventFullDto(getUserEventById(userId, eventId));

        if (eventToUpdate.getState().equals(EventState.PUBLISHED)) {
            throw new IncorrectEventStateAction("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                eventToUpdate.setState(EventState.PUBLISHED);
                eventToUpdate.setPublishedOn(LocalDateTime.now());
            }

            if (updateEventUserRequest.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                eventToUpdate.setState(EventState.PENDING);
            }

            if (updateEventUserRequest.getStateAction().equals(EventStateAction.REJECT_EVENT) ||
                    updateEventUserRequest.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
                eventToUpdate.setState(EventState.CANCELED);
            }
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            eventToUpdate.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getCategory() != null) {
            Category category = CategoryMapper.fromCategoryDto(categoriesService.getCategoryById(updateEventUserRequest.getCategory()));
            eventToUpdate.setCategory(category);
        }

        if (updateEventUserRequest.getDescription() != null) {
            eventToUpdate.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getEventDate() != null) {
            if (ChronoUnit.HOURS.between(LocalDateTime.now(), updateEventUserRequest.getEventDate()) < 2) {
                throw new IncorrectEventDate("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.");
            }
            eventToUpdate.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getLocation() != null) {
            eventToUpdate.setLocation(updateEventUserRequest.getLocation());
        }

        if (updateEventUserRequest.getPaid() != null) {
            eventToUpdate.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getTitle() != null) {
            eventToUpdate.setTitle(updateEventUserRequest.getTitle());
        }

        return EventMapper.toEventFullDto(eventStorage.save(eventToUpdate));
    }

    @Override
    public List<EventFullDto> getEventsByIds(List<Long> eventIds) {
        return eventStorage.findAllById(eventIds).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventsByCategoryId(long catId) {
        return eventStorage.findAllByCategoryId(catId).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}
