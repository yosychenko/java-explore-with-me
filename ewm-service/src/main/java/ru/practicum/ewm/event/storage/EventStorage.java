package ru.practicum.ewm.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;

public interface EventStorage extends JpaRepository<Event, Long> {
}
