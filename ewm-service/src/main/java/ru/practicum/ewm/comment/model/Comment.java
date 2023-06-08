package ru.practicum.ewm.comment.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@Table(name = "comments", schema = "public")
@Getter
@Setter
@EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 7000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;

    @Column
    private CommentState state;

    @Column(nullable = false, insertable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()")
    @CreationTimestamp
    private LocalDateTime updatedOn;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(length = 200)
    private String rejectionReason;

}
