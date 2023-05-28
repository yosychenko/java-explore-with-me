package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Данные нового пользователя
 */
@Data
@Builder
public class NewUserRequest {
    @Email(message = "Email имеет некорректный формат.")
    @NotBlank(message = "Email должен быть заполнен.")
    private String email;
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    @Size(min = 2, max = 250, message = "Имя пользователя должно быть длиной от 2 до 250 символов.")
    private String name;
}
