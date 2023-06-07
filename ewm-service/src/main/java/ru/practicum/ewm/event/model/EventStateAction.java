package ru.practicum.ewm.event.model;

/**
 * Новое состояние события
 */
public enum EventStateAction {
    PUBLISH_EVENT,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW
}
