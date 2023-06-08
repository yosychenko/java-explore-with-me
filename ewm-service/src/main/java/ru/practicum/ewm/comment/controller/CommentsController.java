package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentShortDto> getAllEventComments(
            @RequestParam long eventId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getAllEventComments(eventId, PageRequest.of(from, size));
    }

    @GetMapping("/{commentId}")
    public CommentShortDto getCommentById(@PathVariable long commentId) {
        return commentService.getCommentById(commentId);
    }
}
