package ru.practicum.ewm.comment.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.comment.storage.CommentAdminFilterParams;

import java.util.List;

public interface CommentService {

    List<CommentFullDto> getAllComments(CommentAdminFilterParams params, Pageable pageable);

    List<CommentFullDto> getAllCommentsToReview(Pageable pageable);

    void deleteComment(long commentId);

    CommentFullDto updateCommentState(long commentId, UpdateCommentAdminRequest updateCommentAdminRequest);

    CommentFullDto getCommentById(long commentId);

    CommentShortDto getPublishedCommentById(long commentId);

    List<CommentShortDto> getAllEventComments(long eventId, Pageable pageable);

    List<CommentFullDto> getUserEventComments(long userId, long eventId, Pageable pageable);

    List<CommentFullDto> getAllUserComments(long userId);

    List<CommentFullDto> getAllRejectedComments(long userId);

    CommentFullDto updateRejectedComment(long userId, long commentId, UpdateRejectedCommentUserRequest updateRejectedCommentUserRequest);

    CommentFullDto addUserComment(long userId, long eventId, NewCommentDto newCommentDto);

    CommentFullDto updateUserComment(long userId, long commentId, UpdateCommentUserRequest updateCommentUserRequest);

    void deleteUserComment(long userId, long commentId);

}
