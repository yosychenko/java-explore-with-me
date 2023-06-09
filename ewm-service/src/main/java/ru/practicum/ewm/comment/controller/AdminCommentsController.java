package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.UpdateCommentAdminRequest;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.comment.storage.CommentAdminFilterParams;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentsController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentFullDto> getAllComments(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<Long> events,
            @RequestParam(required = false) List<CommentState> commentStates,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        CommentAdminFilterParams params = CommentAdminFilterParams.builder()
                .users(users)
                .events(events)
                .commentStates(commentStates)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        return commentService.getAllComments(params, PageRequest.of(from, size));
    }

    @GetMapping("/review")
    public List<CommentFullDto> getAllCommentsToReview(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getAllCommentsToReview(PageRequest.of(from, size));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto updateCommentState(
            @PathVariable long commentId,
            @Valid @RequestBody UpdateCommentAdminRequest updateCommentAdminRequest
    ) {
        return commentService.updateCommentState(commentId, updateCommentAdminRequest);
    }

}
