package ru.practicum.ewm.comment.storage;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.comment.model.CommentState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentAdminFilterParams {
    private List<Long> users;
    private List<Long> events;
    private List<CommentState> commentStates;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
