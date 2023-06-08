package ru.practicum.ewm.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.comment.model.CommentStateAction;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UpdateCommentAdminRequest {
    @NotNull
    private CommentStateAction commentStateAction;
    private String rejectionReason;
}
