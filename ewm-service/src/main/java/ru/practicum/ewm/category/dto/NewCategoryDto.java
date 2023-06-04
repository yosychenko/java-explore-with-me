package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Данные для добавления новой категории
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Название категории не должно быть пустым.")
    @Size(min = 1, max = 50, message = "Название категории должно быть длиной от 1 до 50 символов.")
    private String name;
}
