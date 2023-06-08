package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.comment.model.CommentStateAction;
import ru.practicum.ewm.comment.storage.CommentAdminFilterParams;
import ru.practicum.ewm.comment.storage.CommentFilterSpecifications;
import ru.practicum.ewm.comment.storage.CommentStorage;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentStorage commentStorage;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public List<CommentFullDto> getAllComments(CommentAdminFilterParams params, Pageable pageable) {
        Specification<Comment> specification = CommentFilterSpecifications.getCommentsAdminFilterSpecification(params);
        return commentStorage.findAll(specification, pageable).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentFullDto> getAllCommentsToReview(Pageable pageable) {
        return commentStorage.findCommentByState(CommentState.NEEDS_MODERATION, pageable).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(long commentId) {
        Comment commentToDelete = CommentMapper.fromCommentShortDto(getCommentById(commentId));
        commentStorage.delete(commentToDelete);
    }

    @Override
    public CommentFullDto updateCommentState(long commentId, UpdateCommentAdminRequest updateCommentAdminRequest) {
        Comment commentToUpdate = CommentMapper.fromCommentShortDto(getCommentById(commentId));
        CommentStateAction action = updateCommentAdminRequest.getCommentStateAction();
        CommentState existingCommentState = commentToUpdate.getState();

        if (action.equals(CommentStateAction.PUBLISH_COMMENT) && !existingCommentState.equals(CommentState.PENDING)) {
            throw new IncorrectCommentStateActionException("Комментарий можно публиковать, только если он в состоянии ожидания публикации.");
        }

        if (action.equals(CommentStateAction.SEND_COMMENT_TO_USER_REVIEW) && existingCommentState.equals(CommentState.REJECTED)) {
            throw new IncorrectCommentStateActionException("Данный комментарий уже находится на ревью у пользователя.");
        }

        if (action.equals(CommentStateAction.PUBLISH_COMMENT)) {
            commentToUpdate.setState(CommentState.PUBLISHED);
        }

        if (action.equals(CommentStateAction.SEND_COMMENT_TO_USER_REVIEW)) {
            if (updateCommentAdminRequest.getRejectionReason() == null) {
                throw new IncorrectCommentStateActionException("Причина отказа в публикации должна быть указана.");
            } else {
                commentToUpdate.setRejectionReason(updateCommentAdminRequest.getRejectionReason());
            }
            commentToUpdate.setState(CommentState.REJECTED);
        }

        return CommentMapper.toCommentFullDto(commentStorage.save(commentToUpdate));
    }

    @Override
    public CommentShortDto getCommentById(long commentId) {
        Optional<Comment> comment = commentStorage.findById(commentId);
        if (comment.isPresent()) {
            return CommentMapper.toCommentShortDto(comment.get());
        } else {
            throw new EntityNotFoundException(String.format("Комментарий c ID=%s не найден.", commentId));
        }
    }

    @Override
    @Transactional
    public List<CommentShortDto> getAllEventComments(long eventId, Pageable pageable) {
        Event event = EventMapper.fromEventFullDto(eventService.getEventById(eventId));

        return commentStorage.findCommentByEventAndState(event, CommentState.PUBLISHED, pageable)
                .stream()
                .map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentFullDto> getUserEventComments(long userId, long eventId, Pageable pageable) {
        User author = UserMapper.fromUserDto(userService.getUserById(userId));
        Event event = EventMapper.fromEventFullDto(eventService.getEventById(eventId));

        return commentStorage.findCommentsByAuthorAndEvent(author, event, pageable).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentFullDto> getAllUserComments(long userId) {
        User author = UserMapper.fromUserDto(userService.getUserById(userId));

        return commentStorage.findCommentsByAuthor(author).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentFullDto> getAllRejectedComments(long userId) {
        User author = UserMapper.fromUserDto(userService.getUserById(userId));

        return commentStorage.findCommentByAuthorAndState(author, CommentState.REJECTED).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentFullDto updateRejectedComment(long commentId, UpdateRejectedCommentUserRequest updateRejectedCommentUserRequest) {
        Comment commentToUpdate = CommentMapper.fromCommentShortDto(getCommentById(commentId));
        CommentStateAction action = updateRejectedCommentUserRequest.getCommentStateAction();

        if (updateRejectedCommentUserRequest.getText().equalsIgnoreCase(commentToUpdate.getText())) {
            throw new UserCannotUpdateCommentException("Невозможно обновить статус комментария - " +
                    "текст комментария не был изменен.");
        }

        if (action.equals(CommentStateAction.PUBLISH_COMMENT)) {
            throw new UserCannotUpdateCommentException("Невозможно обновить статус комментария - " +
                    "комментарий должен пройти модерацию перед публикацией.");
        }

        if (action.equals(CommentStateAction.SEND_COMMENT_TO_MODERATION)) {
            commentToUpdate.setState(CommentState.PENDING);
        }

        commentToUpdate.setText(updateRejectedCommentUserRequest.getText());

        return CommentMapper.toCommentFullDto(commentStorage.save(commentToUpdate));
    }

    @Override
    public CommentFullDto addUserComment(long userId, long eventId, NewCommentDto newCommentDto) {
        User author = UserMapper.fromUserDto(userService.getUserById(userId));
        Event event = EventMapper.fromEventFullDto(eventService.getEventById(eventId));

        if (!event.isUsersCanComment()) {
            throw new UserCannotLeaveCommentException("Невозможно оставить комментарий - у события отключены комментарии.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new UserCannotLeaveCommentException("Невозможно оставить комментарий - событие не опубликовано.");
        }

        Comment comment = CommentMapper.fromNewCommentDto(newCommentDto);
        comment.setAuthor(author);
        comment.setEvent(event);

        if (!event.getRequestModeration()) {
            comment.setState(CommentState.PUBLISHED);
        } else {
            comment.setState(CommentState.PENDING);
        }

        return CommentMapper.toCommentFullDto(commentStorage.save(comment));
    }

    @Override
    public CommentFullDto updateUserComment(long userId, long commentId, UpdateCommentUserRequest updateCommentUserRequest) {
        User author = UserMapper.fromUserDto(userService.getUserById(userId));
        Comment commentToUpdate = CommentMapper.fromCommentShortDto(getCommentById(commentId));
        Event commentedEvent = commentToUpdate.getEvent();

        if (!commentToUpdate.getState().equals(CommentState.PUBLISHED)) {
            throw new UserCannotUpdateCommentException("Невозможно обновить неопубликованный комментарий.");
        }

        if (!commentToUpdate.getAuthor().equals(author)) {
            throw new UserCannotUpdateCommentException("Невозможно изменить чужой комментарий.");
        }

        commentToUpdate.setText(updateCommentUserRequest.getText());
        commentToUpdate.setUpdatedOn(LocalDateTime.now());

        if (commentedEvent.getRequestModeration()) {
            commentToUpdate.setState(CommentState.NEEDS_MODERATION);
        } else {
            commentToUpdate.setState(CommentState.PUBLISHED);
        }

        return CommentMapper.toCommentFullDto(commentStorage.save(commentToUpdate));

    }

    @Override
    public void deleteUserComment(long userId, long commentId) {
        User author = UserMapper.fromUserDto(userService.getUserById(userId));
        Optional<Comment> comment = commentStorage.findCommentByAuthor(author);

        if (comment.isPresent()) {
            commentStorage.delete(comment.get());
        } else {
            throw new UserCannotDeleteCommentException("Невозможно удалить комментарий - его либо не существует, " +
                    "либо его оставил другой пользователь.");
        }
    }
}
