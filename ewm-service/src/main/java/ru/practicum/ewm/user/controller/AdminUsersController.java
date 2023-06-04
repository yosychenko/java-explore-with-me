package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * API для работы с пользователями
 */
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam List<Long> userIds,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getUsers(userIds, PageRequest.of(from, size));
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
