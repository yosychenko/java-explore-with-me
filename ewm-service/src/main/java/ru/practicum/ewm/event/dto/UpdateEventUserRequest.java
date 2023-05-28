package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.EventStateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Данные для изменения информации о событии.
 * Если поле в запросе не указано (равно null) - значит изменение этих данных не требуется.
 */
@Data
@Builder
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Краткое описание события должно быть длиной от 20 до 2000 символов.")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Описание события должно быть длиной от 20 до 7000 символов.")
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @Size(min = 3, max = 120, message = "Название события должно быть длиной от 20 до 120 символов.")
    private String title;
}
