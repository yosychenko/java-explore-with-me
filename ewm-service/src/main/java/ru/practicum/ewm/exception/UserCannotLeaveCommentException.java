package ru.practicum.ewm.exception;

public class UserCannotLeaveCommentException extends RuntimeException {
    public UserCannotLeaveCommentException(String message) {
        super(message);
    }
}
