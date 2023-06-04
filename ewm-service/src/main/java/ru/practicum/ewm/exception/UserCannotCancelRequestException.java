package ru.practicum.ewm.exception;

public class UserCannotCancelRequestException extends RuntimeException {
    public UserCannotCancelRequestException(String message) {
        super(message);
    }
}
