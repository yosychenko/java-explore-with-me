package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentUserRequest;
import ru.practicum.ewm.comment.dto.UpdateRejectedCommentUserRequest;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCommentsController {
    private final CommentService commentService;

    @GetMapping("/{userId}/comments/{eventId}")
    public List<CommentFullDto> getUserEventComments(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getUserEventComments(userId, eventId, PageRequest.of(from, size));
    }

    @GetMapping("/{userId}/comments")
    public List<CommentFullDto> getAllUserComments(@PathVariable long userId) {
        return commentService.getAllUserComments(userId);
    }

    @GetMapping("/{userId}/comments/rejected")
    public List<CommentFullDto> getAllRejectedComments(@PathVariable long userId) {
        return commentService.getAllRejectedComments(userId);
    }

    @PatchMapping("/{userId}/comments/rejected/{commentId}")
    public CommentFullDto updateRejectedComment(
            @PathVariable long userId,
            @PathVariable long commentId,
            @Valid @RequestBody UpdateRejectedCommentUserRequest updateRejectedCommentUserRequest
    ) {
        return commentService.updateRejectedComment(userId, commentId, updateRejectedCommentUserRequest);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto addUserComment(
            @PathVariable long userId,
            @RequestParam long eventId,
            @Valid @RequestBody NewCommentDto newCommentDto
    ) {
        return commentService.addUserComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentFullDto updateUserComment(
            @PathVariable long userId,
            @PathVariable long commentId,
            @Valid @RequestBody UpdateCommentUserRequest updateCommentUserRequest
    ) {
        return commentService.updateUserComment(userId, commentId, updateCommentUserRequest);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserComment(@PathVariable long userId, @PathVariable long commentId) {
        commentService.deleteUserComment(userId, commentId);
    }

}
