package ru.practicum.stats.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class EndpointHitDto {
    @NotBlank(message = "Название приложения не должно быть пустым.")
    @Size(max = 512, message = "Название приложения не должно превышать 512 символов.")
    private String app;

    @NotBlank(message = "URI не должна быть пустой.")
    @Size(max = 1024, message = "Ссылка на эндпоинт не должна превышать 1024 символов.")
    private String uri;

    @NotBlank(message = "IP-адрес не должен быть пустым.")
    @Size(max = 45, message = "IP-адреc не должен превышать 45 символов.")
    private String ip;
    private LocalDateTime timestamp;
}
