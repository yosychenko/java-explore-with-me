package ru.practicum.ewm.comment.mapper;

import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.user.mapper.UserMapper;

public class CommentMapper {
    public static CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .event(EventMapper.toEventFullDto(comment.getEvent()))
                .state(comment.getState())
                .updatedOn(comment.getUpdatedOn())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .updatedOn(comment.getUpdatedOn())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public static Comment fromCommentShortDto(CommentShortDto commentShortDto) {
        Comment comment = new Comment();
        comment.setId(commentShortDto.getId());
        comment.setText(commentShortDto.getText());
        comment.setAuthor(UserMapper.fromUserShortDto(commentShortDto.getAuthor()));
        comment.setEvent(EventMapper.fromEventShortDto(commentShortDto.getEvent()));
        comment.setUpdatedOn(commentShortDto.getUpdatedOn());
        comment.setCreatedOn(commentShortDto.getCreatedOn());
        return comment;
    }

    public static Comment fromNewCommentDto(NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        return comment;
    }
}
