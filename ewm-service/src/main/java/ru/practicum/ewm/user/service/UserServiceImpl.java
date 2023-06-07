package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.DuplicateEntityException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<UserDto> getUsers(List<Long> userIds, Pageable pageable) {
        List<User> users;
        if (userIds == null) {
            users = userStorage.findAll(pageable).getContent();
        } else {
            users = userStorage.findAllByIdIn(userIds, pageable);
        }
        return users
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        Optional<User> user = userStorage.findById(userId);
        if (user.isPresent()) {
            return UserMapper.toUserDto(user.get());
        } else {
            throw new EntityNotFoundException(String.format("Пользователь с ID=%s не найден.", userId));
        }
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        User user = UserMapper.fromNewUserRequest(newUserRequest);
        try {
            User addedUser = userStorage.save(user);
            return UserMapper.toUserDto(addedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEntityException(String.format("Пользователь с email=%s уже существует.", user.getEmail()));
        }
    }

    @Override
    public void deleteUser(long userId) {
        Optional<User> userToDelete = userStorage.findById(userId);
        if (userToDelete.isPresent()) {
            userStorage.deleteById(userId);
        } else {
            throw new EntityNotFoundException(String.format("Пользователь c ID=%s не найден.", userId));
        }
    }


}
