package ru.practicum.ewm.exception;

public class UserCannotChangeRequestStatusException extends RuntimeException {
    public UserCannotChangeRequestStatusException(String message) {
        super(message);
    }
}
