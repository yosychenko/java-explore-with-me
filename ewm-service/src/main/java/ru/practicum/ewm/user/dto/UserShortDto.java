package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Пользователь (краткая информация)
 */
@Data
@Builder
public class UserShortDto {
    private long id;
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    @Size(min = 2, max = 250, message = "Имя пользователя должно быть длиной от 2 до 250 символов.")
    private String name;
}
