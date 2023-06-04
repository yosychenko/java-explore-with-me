package ru.practicum.ewm.request.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@Table(name = "participation_requests", schema = "public")
@Getter
@Setter
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;

    @Column(nullable = false)
    private EventState status;
}
