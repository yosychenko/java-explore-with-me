package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Изменение информации о подборке событий.
 * Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Заголовок подборки событий должен быть длиной от 1 до 50 символов.")
    private String title;
}
