package ru.practicum.ewm.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventStorage extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByCategoryId(long catId);
}
