package ru.practicum.ewm.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> userIds, Pageable pageable);

    UserDto getUserById(long userId);

    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);
}
