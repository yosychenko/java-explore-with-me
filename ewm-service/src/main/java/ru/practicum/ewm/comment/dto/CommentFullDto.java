package ru.practicum.ewm.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentFullDto {
    private long id;
    private String text;
    private UserDto author;
    private EventFullDto event;
    private CommentState state;
    private LocalDateTime updatedOn;
    private LocalDateTime createdOn;
}
