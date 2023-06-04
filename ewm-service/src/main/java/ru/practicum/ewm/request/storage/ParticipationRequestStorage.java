package ru.practicum.ewm.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface ParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequestDto> findAllByRequester(User user);

}
