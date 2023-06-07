package ru.practicum.ewm.request.service;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ConfirmedRequestsForEvent;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> getParticipationRequests(long userId);

    ParticipationRequestDto getParticipationRequestById(long requestId);

    ParticipationRequestDto addParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);

    List<ParticipationRequestDto> getUserEventParticipationRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateUserEventParticipationRequestStatus(
            long userId,
            long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    );
}
