package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    private User add(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/users")
    private User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/users")
    private List<User> getUsers() {
        return userService.getUsers();
    }

}

