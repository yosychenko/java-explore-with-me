package ru.practicum.ewm.exception;

public class UserCannotUpdateCommentException extends RuntimeException {
    public UserCannotUpdateCommentException(String message) {
        super(message);
    }
}
