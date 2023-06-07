package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.DuplicateEntityException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.UserCannotChangeRequestStatusException;
import ru.practicum.ewm.exception.UserCannotParticipateInEventException;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.ParticipationRequestStatus;
import ru.practicum.ewm.request.storage.ParticipationRequestStorage;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestStorage participationRequestStorage;
    private final UserService userService;
    private final EventService eventService;

    @Override
    @Transactional
    public List<ParticipationRequestDto> getParticipationRequests(long userId) {
        User requester = UserMapper.fromUserDto(userService.getUserById(userId));
        return participationRequestStorage.findAllByRequester(requester).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto getParticipationRequestById(long requestId) {
        Optional<ParticipationRequest> request = participationRequestStorage.findById(requestId);
        if (request.isPresent()) {
            return ParticipationRequestMapper.toParticipationRequestDto(request.get());
        } else {
            throw new EntityNotFoundException(String.format("Запрос с ID=%s не найден", requestId));
        }
    }

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
        User requester = UserMapper.fromUserDto(userService.getUserById(userId));
        Event event = EventMapper.fromEventFullDto(eventService.getEventById(eventId));

        long eventRequestsNum = participationRequestStorage.findCountOfConfirmedRequests(event, ParticipationRequestStatus.CONFIRMED);

        if (event.getInitiator().equals(requester)) {
            throw new UserCannotParticipateInEventException("Инициатор события не может добавить запрос на участие в своём событии.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new UserCannotParticipateInEventException("Нельзя участвовать в неопубликованном событии.");
        }

        if (eventRequestsNum >= event.getParticipantLimit() && event.getParticipantLimit() > 0) {
            event.setAvailable(false);
            eventService.updateEvent(event);
            throw new UserCannotParticipateInEventException("У события достигнут лимит запросов на участие.");
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
            long confirmedRequests = event.getConfirmedRequests();
            event.setConfirmedRequests(++confirmedRequests);
            eventService.updateEvent(event);
        }

        try {
            return ParticipationRequestMapper.toParticipationRequestDto(participationRequestStorage.save(participationRequest));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEntityException(String.format("Запрос на участие пользователя c ID=%s в событии с ID=%s уже существует.", userId, eventId));
        }
    }


    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        User requester = UserMapper.fromUserDto(userService.getUserById(userId));
        ParticipationRequestDto participationRequestDto = getParticipationRequestById(requestId);

        if (participationRequestDto.getRequester() != userId) {
            throw new UserCannotChangeRequestStatusException("Пользователь с ID=%s не может отменить запрос с ID=%s - он не является его автором.");
        }

        Event event = EventMapper.fromEventFullDto(eventService.getEventById(participationRequestDto.getEvent()));
        ParticipationRequest request = ParticipationRequestMapper.fromParticipationRequestDto(
                getParticipationRequestById(requestId),
                event,
                requester
        );
        request.setStatus(ParticipationRequestStatus.CANCELED);
        long confirmedRequests = event.getConfirmedRequests();
        event.setConfirmedRequests(--confirmedRequests);

        if (!event.isAvailable()) {
            event.setAvailable(true);
        }

        eventService.updateEvent(event);

        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestStorage.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getUserEventParticipationRequests(long userId, long eventId) {
        return participationRequestStorage.findAllUserEventRequests(userId, eventId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateUserEventParticipationRequestStatus(
            long userId,
            long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        List<ParticipationRequest> requests = participationRequestStorage.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
        Event event = EventMapper.fromEventFullDto(eventService.getUserEventById(userId, eventId));
        User user = UserMapper.fromUserDto(userService.getUserById(userId));

        long eventRequestsNum = participationRequestStorage.findCountOfConfirmedRequests(event, ParticipationRequestStatus.CONFIRMED);

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(confirmedRequests)
                    .rejectedRequests(rejectedRequests)
                    .build();
        }

        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new UserCannotChangeRequestStatusException("Cтатус можно изменить только у заявок, находящихся в состоянии ожидания");
            }

            if (eventRequestStatusUpdateRequest.getStatus().equals(ParticipationRequestStatus.CONFIRMED) &&
                    eventRequestsNum >= event.getParticipantLimit() && event.getParticipantLimit() > 0) {
                event.setAvailable(false);
                eventService.updateEvent(event);
                throw new UserCannotChangeRequestStatusException("У события достигнут лимит запросов на участие.");
            }

            request.setStatus(eventRequestStatusUpdateRequest.getStatus());
            if (eventRequestStatusUpdateRequest.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                confirmedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(request));
            } else {
                rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(request));
            }
        }

        if (!confirmedRequests.isEmpty()) {
            participationRequestStorage.saveAll(
                    confirmedRequests.stream()
                            .map(req ->
                                    ParticipationRequestMapper.fromParticipationRequestDto(req, event, user)
                            )
                            .collect(Collectors.toList())
            );
            long eventConfirmedRequests = event.getConfirmedRequests();
            event.setConfirmedRequests(eventConfirmedRequests + confirmedRequests.size());
            eventService.updateEvent(event);
        }

        if (!rejectedRequests.isEmpty()) {
            participationRequestStorage.saveAll(
                    rejectedRequests.stream()
                            .map(req ->
                                    ParticipationRequestMapper.fromParticipationRequestDto(req, event, user)
                            )
                            .collect(Collectors.toList())
            );

            long eventConfirmedRequests = event.getConfirmedRequests();
            event.setConfirmedRequests(eventConfirmedRequests - confirmedRequests.size());
            eventService.updateEvent(event);
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}
