package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Данные нового пользователя
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Email(message = "Email имеет некорректный формат.")
    @NotBlank(message = "Email должен быть заполнен.")
    @Size(min = 6, max = 254, message = "Email должен быть от 6 до 254 символов.")
    private String email;
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    @Size(min = 2, max = 250, message = "Имя пользователя должно быть длиной от 2 до 250 символов.")
    private String name;
}
