package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.storage.ParticipationRequestStorage;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

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
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
        User requester = UserMapper.fromUserDto(userService.getUserById(userId));
        Event event = EventMapper.fromEventFullDto(eventService.getEventById(eventId));

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        // participationRequest.setStatus();


        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestStorage.save(participationRequest));
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        return null;
    }
}
