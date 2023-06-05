package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidatorUser;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
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
        return userRepository.updateUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getAllUsers();
    }
}
