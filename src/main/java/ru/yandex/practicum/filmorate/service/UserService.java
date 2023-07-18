package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User update(User user);

    List<User> getUsers();

    User getUserById(Long userId);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriend(Long userId);

    List<User> getListOfMutualFriends(Long userId, Long otherId);
}
