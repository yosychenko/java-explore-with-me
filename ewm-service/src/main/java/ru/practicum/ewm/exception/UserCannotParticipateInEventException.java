package ru.practicum.ewm.exception;

public class UserCannotParticipateInEventException extends RuntimeException {
    public UserCannotParticipateInEventException(String message) {
        super(message);
    }
}
