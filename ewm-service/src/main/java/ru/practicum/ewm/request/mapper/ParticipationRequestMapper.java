package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated())
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }

    public static ParticipationRequest fromParticipationRequestDto(
            ParticipationRequestDto participationRequestDto,
            Event event,
            User requester
    ) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setId(participationRequestDto.getId());
        participationRequest.setCreated(participationRequestDto.getCreated());
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);
        participationRequest.setStatus(participationRequestDto.getStatus());

        return participationRequest;
    }
}
