package ru.practicum.ewm.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventStorage extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByCategoryId(long catId);
    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    Optional<Event> findEventByIdAndState(long eventId, EventState state);

    Optional<Event> findEventByInitiatorIdAndId(long initiatorId, long eventId);
}
