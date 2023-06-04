package ru.practicum.ewm.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoriesService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSortType;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventStateAction;
import ru.practicum.ewm.event.storage.EventAdminFilterParams;
import ru.practicum.ewm.event.storage.EventSpecifications;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.IncorrectEventStateAction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventStorage eventStorage;
    private final CategoriesService categoriesService;

    @Autowired
    public EventServiceImpl(EventStorage eventStorage, @Lazy CategoriesService categoriesService) {
        this.eventStorage = eventStorage;
        this.categoriesService = categoriesService;
    }

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
    public List<EventFullDto> getEvents(EventAdminFilterParams params, Pageable pageable) {
        Specification<Event> specification = EventSpecifications.getEventsAdminFilterSpecification(params);
        return eventStorage.findAll(specification, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
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

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event eventToUpdate = EventMapper.fromEventFullDto(getEventById(eventId));
        LocalDateTime publishedOn = LocalDateTime.now();

        if (updateEventAdminRequest.getStateAction().equals(EventStateAction.PUBLISH_EVENT) &&
                !eventToUpdate.getState().equals(EventState.PENDING)) {
            throw new IncorrectEventStateAction("Событие можно публиковать, только если оно в состоянии ожидания публикации.");
        }

        if (updateEventAdminRequest.getStateAction().equals(EventStateAction.PUBLISH_EVENT) &&
                ChronoUnit.HOURS.between(publishedOn, eventToUpdate.getEventDate()) < 1) {
            throw new IncorrectEventStateAction("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }

        if (updateEventAdminRequest.getStateAction().equals(EventStateAction.REJECT_EVENT) &&
                eventToUpdate.getState().equals(EventState.PUBLISHED)) {
            throw new IncorrectEventStateAction("Событие можно отклонить, только если оно еще не опубликовано.");
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

        if (updateEventAdminRequest.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
            eventToUpdate.setState(EventState.PUBLISHED);
        }

        if (updateEventAdminRequest.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
            eventToUpdate.setState(EventState.CANCELED);
        }

        if (updateEventAdminRequest.getTitle() != null) {
            eventToUpdate.setTitle(updateEventAdminRequest.getTitle());
        }

        return EventMapper.toEventFullDto(eventStorage.save(eventToUpdate));

    }
}
