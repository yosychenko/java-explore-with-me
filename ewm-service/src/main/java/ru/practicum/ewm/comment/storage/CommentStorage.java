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
    Optional<Comment> findCommentByIdAndState(long commentId, CommentState state);

    List<Comment> findCommentByStateOrderByCreatedOnDesc(CommentState commentState, Pageable pageable);

    List<Comment> findCommentByEventAndStateOrderByCreatedOnDesc(Event event, CommentState commentState, Pageable pageable);

    List<Comment> findCommentByAuthorAndStateOrderByCreatedOnDesc(User author, CommentState commentState);

    List<Comment> findCommentsByAuthorOrderByCreatedOnDesc(User author);

    Optional<Comment> findCommentByIdAndAuthor(long commentId, User author);

    List<Comment> findCommentsByAuthorAndEventOrderByCreatedOnDesc(User author, Event event, Pageable pageable);


}
