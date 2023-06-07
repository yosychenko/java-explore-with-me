package ru.practicum.ewm.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Подборка событий
 */
@Data
@Builder
public class CompilationDto {
    private long id;
    private boolean pinned;
    @NotBlank(message = "Заголовок подборки событий не должен быть пустым.")
    private String title;
    private List<EventShortDto> events;
}