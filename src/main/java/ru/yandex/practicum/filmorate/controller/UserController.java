package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    private User add(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    private User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    private List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}")
    private User getUserById(@PathVariable("id") Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    private void addFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    private void deleteFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    private List<User> getFriend(@PathVariable("id") Long userId) {
        return userService.getFriend(userId);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    private List<User> getListOfMutualFriends(@PathVariable("id") Long userId, @PathVariable Long otherId) {
        return userService.getListOfMutualFriends(userId, otherId);
    }
}

