package ru.practicum.ewm;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private static final String EXCEPTION_MESSAGE_FIELD = "message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBindValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler({IncorrectEventDateException.class, IncorrectDateRangeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(RuntimeException ex) {
        return Map.of(EXCEPTION_MESSAGE_FIELD, ex.getMessage());
    }

    @ExceptionHandler({
            DuplicateEntityException.class,
            EventsOfDeletedCategoryExistException.class,
            IncorrectEventStateActionException.class,
            UserCannotParticipateInEventException.class,
            UserCannotChangeRequestStatusException.class,
            IncorrectCommentStateActionException.class,
            UserCannotLeaveCommentException.class,
            UserCannotDeleteCommentException.class,
            UserCannotUpdateCommentException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflictExceptions(RuntimeException ex) {
        return Map.of(EXCEPTION_MESSAGE_FIELD, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundExceptions(RuntimeException ex) {
        return Map.of(EXCEPTION_MESSAGE_FIELD, ex.getMessage());
    }

    @ExceptionHandler(StatsServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalServerError(RuntimeException ex) {
        return Map.of(EXCEPTION_MESSAGE_FIELD, ex.getMessage());
    }
}
