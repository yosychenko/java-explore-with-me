package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Полная информация о событии
 */
@Data
@Builder
public class EventFullDto {
    private Long id;
    @NotBlank(message = "Краткое описание события не должно быть пустым.")
    private String annotation;
    @NotNull(message = "Категория события должна быть заполнена.")
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @NotNull(message = "Дата и время начала события не должны быть пустыми.")
    private LocalDateTime eventDate;
    @NotNull(message = "Инициатор события должен быть заполнен.")
    private UserShortDto initiator;
    @NotNull(message = "Место проведения события не должно быть пустым.")
    private Location location;
    private boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    @NotBlank(message = "Название события не должно быть пустым.")
    private String title;
    private Long views;
}
