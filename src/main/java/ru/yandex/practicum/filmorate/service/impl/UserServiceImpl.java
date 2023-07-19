package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidatorUser;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;


@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userRepository;

    @Autowired
    public UserServiceImpl(UserStorage userRepository) {
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
        validatorId(user.getId());
        return userRepository.updateUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Long userId) {
        validatorId(userId);
        return userRepository.getUserById(userId);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        validatorId(userId);
        validatorId(friendId);
        userRepository.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        validatorId(userId);
        validatorId(friendId);
        userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriend(Long userId) {
        validatorId(userId);
        return userRepository.getFriends(userId);
    }

    @Override
    public List<User> getListOfMutualFriends(Long userId, Long otherId) {
        validatorId(userId);
        validatorId(otherId);
        return userRepository.getListOfMutualFriends(userId, otherId);
    }

    private void validatorId(Long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            log.warn("Error");
            log.warn("userId: " + userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
