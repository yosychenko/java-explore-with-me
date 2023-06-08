package ru.practicum.ewm.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentShortDto {
    private long id;
    private String text;
    private UserShortDto author;
    private EventShortDto event;
    private LocalDateTime updatedOn;
    private LocalDateTime createdOn;
}
