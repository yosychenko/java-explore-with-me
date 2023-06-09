package ru.practicum.ewm.event.controller.utils;

import ru.practicum.ewm.exception.IncorrectDateRangeException;

import java.time.LocalDateTime;

public class DateRangeValidator {
    public static void validate(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new IncorrectDateRangeException("Временной промежуток задан некорректно.");
        }
    }
}
