package ru.practicum.ewm.exception;

public class EventsOfDeletedCategoryExistException extends RuntimeException {
    public EventsOfDeletedCategoryExistException(long catId) {
        super(String.format("Невозможно удалить категорию с ID=%s - существуют связанные с ней события.", catId));
    }
}
