package ru.practicum.ewm.exception;

public class UserCannotDeleteCommentException extends RuntimeException {
    public UserCannotDeleteCommentException(String message) {
        super(message);
    }
}
