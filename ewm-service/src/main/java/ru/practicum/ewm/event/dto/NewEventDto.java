package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Новое событие
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Краткое описание события не должно быть пустым.")
    @Size(min = 20, max = 2000, message = "Краткое описание события должно быть длиной от 20 до 2000 символов.")
    private String annotation;
    private long category;
    @NotBlank(message = "Описание события не должно быть пустым.")
    @Size(min = 20, max = 7000, message = "Описание события должно быть длиной от 20 до 7000 символов.")
    private String description;
    @NotNull(message = "Дата и время начала события не должны быть пустыми.")
    private LocalDateTime eventDate;
    @NotNull(message = "Место проведения события не должно быть пустым.")
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank(message = "Название события не должно быть пустым.")
    @Size(min = 3, max = 120, message = "Название события должно быть длиной от 20 до 120 символов.")
    private String title;
}
