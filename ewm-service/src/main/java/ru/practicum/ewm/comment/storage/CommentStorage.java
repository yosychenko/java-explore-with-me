package ru.practicum.ewm.comment.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface CommentStorage extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findCommentByState(CommentState commentState, Pageable pageable);

    List<Comment> findCommentByEventAndState(Event event, CommentState commentState, Pageable pageable);

    List<Comment> findCommentByAuthorAndState(User author, CommentState commentState);

    List<Comment> findCommentsByAuthor(User author);

    Optional<Comment> findCommentByAuthor(User author);

    List<Comment> findCommentsByAuthorAndEvent(User author, Event event, Pageable pageable);


}
