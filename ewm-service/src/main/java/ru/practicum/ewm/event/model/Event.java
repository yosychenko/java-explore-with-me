package ru.practicum.ewm.event.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.Location;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "events", schema = "public")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "initiator", referencedColumnName = "id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    @Embedded
    private Location location;

    @Column
    private Boolean paid;

    @Column
    private Integer participantLimit;

    @Column
    private Boolean requestModeration;

    @Column
    private EventState state;

    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;

    private long views;

    private long confirmedRequests;
}

