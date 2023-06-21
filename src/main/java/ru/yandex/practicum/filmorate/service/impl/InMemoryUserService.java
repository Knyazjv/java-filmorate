package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidatorUser;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class InMemoryUserService implements UserService {

    private final UserStorage userRepository;

    @Autowired
    public InMemoryUserService(UserStorage userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        ValidatorUser.validateUser(user);
        return userRepository.createUser(user);
    }

    @Override
    public User update(User user) {
        ValidatorUser.validateUser(user);
        if (userRepository.getUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь отсутствует");
        }

        return userRepository.updateUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new NotFoundException("Пользователь с id:" + userId + " отсутствует");
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.getUserById(userId);
        User friend = userRepository.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь отсутствует");
        }
        userRepository.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (userRepository.getUserById(userId) == null || userRepository.getUserById(friendId) == null) {
            throw new NotFoundException("Пользователь отсутствует");
        } else {
            userRepository.deleteFriend(userId, friendId);
        }
    }

    @Override
    public List<User> getFriend(Long userId) {
        List<User> friends = new ArrayList<>();
        User user = userRepository.getUserById(userId);
        if (user == null) throw new NotFoundException("Пользователь с id:" + userId + " отсутствует");
        List<Long> friendIds = userRepository.getFriends(userId);
        for (Long idFriend : friendIds) {
            user = userRepository.getUserById(idFriend);
            if (user != null) friends.add(user);
        }
        return friends;
    }

    @Override
    public List<User> getListOfMutualFriends(Long userId, Long otherId) {
        List<User> mutualFriends = new ArrayList<>();
        User user = userRepository.getUserById(userId);
        User otherUser = userRepository.getUserById(otherId);
        if (user == null || otherUser == null) throw new NotFoundException("Пользователь не найден");
        List<Long> idMutualFriends = userRepository.getListOfMutualFriends(userId, otherId);
        if (idMutualFriends == null) return mutualFriends;
        if (!idMutualFriends.isEmpty()) {
            for (Long idMutualFriend : userRepository.getListOfMutualFriends(userId, otherId)) {
                user = userRepository.getUserById(idMutualFriend);
                if (user != null) mutualFriends.add(user);
            }
        }
        return mutualFriends;
    }
}
