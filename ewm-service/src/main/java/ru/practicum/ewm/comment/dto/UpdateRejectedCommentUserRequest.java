package ru.practicum.ewm.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.comment.model.CommentStateAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateRejectedCommentUserRequest {
    @NotBlank(message = "Текст комментария не должен быть пустым")
    @Size(min = 1, max = 7000, message = "Текст комментария должен быть от 1 до 7000 символов.")
    private String text;

    private CommentStateAction commentStateAction;
}
