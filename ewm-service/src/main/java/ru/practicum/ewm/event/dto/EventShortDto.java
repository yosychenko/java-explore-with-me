package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Краткая информация о событии
 */
@Data
@Builder
public class EventShortDto {
    private Long id;
    @NotBlank(message = "Краткое описание события не должно быть пустым.")
    private String annotation;
    @NotNull(message = "Категория события должна быть заполнена.")
    private CategoryDto category;
    private Long confirmedRequests = 0L;
    @NotBlank(message = "Дата и время начала события не должны быть пустыми.")
    private LocalDateTime eventDate;
    @NotNull(message = "Инициатор события должен быть заполнен.")
    private UserShortDto initiator;
    private boolean paid;
    @NotBlank(message = "Название события не должно быть пустым.")
    private String title;
    private Long views = 0L;
}
