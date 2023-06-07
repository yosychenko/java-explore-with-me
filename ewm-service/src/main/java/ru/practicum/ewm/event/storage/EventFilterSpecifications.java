package ru.practicum.ewm.event.storage;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSortType;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventFilterSpecifications {
    public static Specification<Event> getEventsAdminFilterSpecification(EventAdminFilterParams params) {
        return (event, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getUsers() != null) {
                predicates.add(cb.in(event.get("initiator").get("id")).value(params.getUsers()));
            }

            if (params.getStates() != null) {
                predicates.add(cb.in(event.get("state")).value(params.getStates()));
            }

            if (params.getCategories() != null) {
                predicates.add(cb.in(event.get("category").get("id")).value(params.getCategories()));
            }

            if (params.getRangeStart() != null && params.getRangeEnd() != null) {
                predicates.add(cb.between(event.get("eventDate"), params.getRangeStart(), params.getRangeEnd()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Event> getEventsFilterSpecification(EventFilterParams params) {
        return (event, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getText() != null) {
                Predicate annotationSearch = cb.like(cb.lower(event.get("annotation")), "%" + params.getText().toLowerCase() + "%");
                Predicate descriptionSearch = cb.like(cb.lower(event.get("description")), "%" + params.getText().toLowerCase() + "%");
                predicates.add(cb.or(annotationSearch, descriptionSearch));
            }

            if (params.getCategories() != null) {
                predicates.add(cb.in(event.get("category").get("id")).value(params.getCategories()));
            }

            if (params.getPaid() != null) {
                predicates.add(cb.equal(event.get("paid"), params.getPaid()));
            }

            if (params.getRangeStart() != null && params.getRangeEnd() != null) {
                predicates.add(cb.between(event.get("eventDate"), params.getRangeStart(), params.getRangeEnd()));
            } else {
                predicates.add(cb.greaterThan(event.get("eventDate"), LocalDateTime.now()));
            }

            if (params.isOnlyAvailable()) {
                predicates.add(cb.equal(event.get("isAvailable"), true));
            }

            if (params.getSort() != null) {
                if (params.getSort().equals(EventSortType.EVENT_DATE)) {
                    query.orderBy(cb.asc(event.get("eventDate")));
                } else if ((params.getSort().equals(EventSortType.VIEWS))) {
                    query.orderBy(cb.desc(event.get("views")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }
}
