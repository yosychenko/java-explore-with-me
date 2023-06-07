package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Подборка событий
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events = new ArrayList<>();
    private boolean pinned = false;
    @NotBlank(message = "Заголовок подборки событий не должен быть пустым.")
    @Size(min = 1, max = 50, message = "Заголовок подборки событий должен быть длиной от 1 до 50 символов.")
    private String title;
}
