package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.DuplicateEntityException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.UserCannotCancelRequestException;
import ru.practicum.ewm.exception.UserCannotParticipateInEventException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.ParticipationRequestStatus;
import ru.practicum.ewm.request.storage.ParticipationRequestStorage;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestStorage participationRequestStorage;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(long userId) {
        User requester = UserMapper.fromUserDto(userService.getUserById(userId));
        return participationRequestStorage.findAllByRequester(requester);
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
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
        User requester = UserMapper.fromUserDto(userService.getUserById(userId));
        Event event = EventMapper.fromEventFullDto(eventService.getEventById(eventId));
        // +1 потенциальный этот запрос
        int eventRequestsNum = participationRequestStorage.countByEvent(event) + 1;

        if (event.getInitiator().equals(requester)) {
            throw new UserCannotParticipateInEventException("Инициатор события не может добавить запрос на участие в своём событии.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new UserCannotParticipateInEventException("Нельзя участвовать в неопубликованном событии.");
        }

        if (eventRequestsNum > event.getParticipantLimit()) {
            throw new UserCannotParticipateInEventException("У события достигнут лимит запросов на участие.");
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        if (!event.getRequestModeration()) {
            participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
        }

        try {
            return ParticipationRequestMapper.toParticipationRequestDto(participationRequestStorage.save(participationRequest));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEntityException(String.format("Запрос на участие пользователя c ID=%s в событии с ID=%s уже существует.", userId, eventId));
        }
    }


    @Override
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        User requester = UserMapper.fromUserDto(userService.getUserById(userId));
        ParticipationRequestDto participationRequestDto = getParticipationRequestById(requestId);

        if (participationRequestDto.getRequester() != userId) {
            throw new UserCannotCancelRequestException("Пользователь с ID=%s не может отменить запрос с ID=%s - он не является его автором.");
        }

        Event event = EventMapper.fromEventFullDto(eventService.getEventById(participationRequestDto.getEvent()));
        ParticipationRequest request = ParticipationRequestMapper.fromParticipationRequestDto(
                getParticipationRequestById(requestId),
                event,
                requester
        );
        request.setStatus(ParticipationRequestStatus.PENDING);

        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestStorage.save(request));
    }
}
