package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private Integer idUser = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(idUser);
        users.put(idUser++, user);
        log.info("Пользователь успешно добавлен");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if(users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлен");
            return user;
        }
        throw new ValidationException("Пользователь не найден");
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
