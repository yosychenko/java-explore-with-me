package ru.practicum.ewm.exception;

public class IncorrectEventStateAction extends RuntimeException {
    public IncorrectEventStateAction(String message) {
        super(message);
    }
}
