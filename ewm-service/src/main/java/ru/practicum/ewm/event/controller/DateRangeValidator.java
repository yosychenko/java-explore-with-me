package ru.practicum.ewm.event.controller;

import ru.practicum.ewm.exception.IncorrectDateRange;

import java.time.LocalDateTime;

public class DateRangeValidator {
    public static void validate(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new IncorrectDateRange("Временной промежуток задан некорректно.");
        }
    }
}
