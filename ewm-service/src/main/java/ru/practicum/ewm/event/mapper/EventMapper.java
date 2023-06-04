package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.mapper.UserMapper;

public class EventMapper {
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(
                        CategoryDto.builder()
                                .id(event.getCategory().getId())
                                .name(event.getCategory().getName())
                                .build()
                )
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(
                        UserShortDto.builder()
                                .id(event.getInitiator().getId())
                                .name(event.getInitiator().getName())
                                .build()
                )
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(
                        CategoryDto.builder()
                                .id(event.getCategory().getId())
                                .name(event.getCategory().getName())
                                .build()
                )
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(
                        UserShortDto.builder()
                                .id(event.getInitiator().getId())
                                .name(event.getInitiator().getName())
                                .build()
                )
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event fromEventShortDto(EventShortDto eventShortDto) {
        Event event = new Event();
        event.setId(eventShortDto.getId());
        event.setAnnotation(eventShortDto.getAnnotation());
        event.setCategory(CategoryMapper.fromCategoryDto(eventShortDto.getCategory()));
        event.setConfirmedRequests(eventShortDto.getConfirmedRequests());
        event.setEventDate(eventShortDto.getEventDate());
        event.setInitiator(UserMapper.fromUserShortDto(eventShortDto.getInitiator()));
        event.setPaid(eventShortDto.isPaid());
        event.setTitle(eventShortDto.getTitle());
        event.setViews(eventShortDto.getViews());
        return event;
    }

    public static Event fromEventFullDto(EventFullDto eventFullDto) {
        Event event = new Event();
        event.setId(eventFullDto.getId());
        event.setAnnotation(eventFullDto.getAnnotation());
        event.setCategory(CategoryMapper.fromCategoryDto(eventFullDto.getCategory()));
        event.setConfirmedRequests(eventFullDto.getConfirmedRequests());
        event.setDescription(eventFullDto.getDescription());
        event.setEventDate(eventFullDto.getEventDate());
        event.setInitiator(UserMapper.fromUserShortDto(eventFullDto.getInitiator()));
        event.setPaid(eventFullDto.isPaid());
        event.setParticipantLimit(event.getParticipantLimit());
        event.setRequestModeration(event.getRequestModeration());
        event.setTitle(eventFullDto.getTitle());
        event.setViews(eventFullDto.getViews());
        return event;
    }
}
