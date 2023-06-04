package ru.practicum.ewm.event.storage;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EventSpecifications {
    public static Specification<Event> getEventsAdminFilterSpecification(EventAdminFilterParams params) {
        return (event, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getUsers() != null) {
                predicates.add(criteriaBuilder.in(event.get("initiator").get("id")).value(params.getUsers()));
            }

            if (params.getStates() != null) {
                predicates.add(criteriaBuilder.in(event.get("state")).value(params.getStates()));
            }

            if (params.getCategories() != null) {
                predicates.add(criteriaBuilder.in(event.get("category").get("id")).value(params.getCategories()));
            }

            if (params.getRangeStart() != null && params.getRangeEnd() != null) {
                predicates.add(criteriaBuilder.between(event.get("eventDate"), params.getRangeStart(), params.getRangeEnd()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
