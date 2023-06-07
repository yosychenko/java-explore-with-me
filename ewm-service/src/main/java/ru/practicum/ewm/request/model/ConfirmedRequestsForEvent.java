package ru.practicum.ewm.request.model;

import lombok.*;
import ru.practicum.ewm.event.model.Event;

@Data
@Builder
@EqualsAndHashCode
@Value
@AllArgsConstructor
@Getter
public class ConfirmedRequestsForEvent {
    Event event;
    Long confirmedRequests;
}
