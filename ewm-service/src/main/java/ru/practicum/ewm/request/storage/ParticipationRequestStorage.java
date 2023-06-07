package ru.practicum.ewm.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.ParticipationRequestStatus;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface ParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester(User user);

    @Query(
            "SELECT new ParticipationRequest(req.id, req.created, req.event, req.requester, req.status) " +
                    "FROM ParticipationRequest req " +
                    "JOIN Event e " +
                    "ON e.id = req.event.id " +
                    "WHERE e.initiator.id = :initiatorId AND e.id = :eventId"
    )
    List<ParticipationRequest> findAllUserEventRequests(long initiatorId, long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> requestIds);

    @Query("SELECT COUNT(*) from ParticipationRequest req WHERE req.event = :event AND req.status = :status")
    long findCountOfConfirmedRequests(Event event, ParticipationRequestStatus status);

}
