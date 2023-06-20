package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {

    private Long idUser = 1L;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> userFriendIds = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(idUser);
        users.put(idUser++, user);
        log.info("Пользователь успешно добавлен");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
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

    @Override
    public User getUserById(Long userId) {
         return users.getOrDefault(userId, null);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        addFriendId(userId,friendId);
        addFriendId(friendId, userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (!userFriendIds.containsKey(userId)) throw new NotFoundException("Id: " + userId + "не найден");
        if (!userFriendIds.containsKey(friendId)) throw new NotFoundException("Id: " + friendId + "не найден");
        deleteFriendId(userId, friendId);
        deleteFriendId(friendId, userId);
    }

    @Override
    public List<Long> getFriends(Long userId) {
        if (userFriendIds.isEmpty() || userFriendIds.get(userId).isEmpty()) return null;
        return new ArrayList<>(userFriendIds.get(userId));
    }

    @Override
    public List<Long> getListOfMutualFriends(Long userId, Long otherId) {
        if (userFriendIds.isEmpty()) return null;
        Set<Long> userIds = userFriendIds.get(userId);
        Set<Long> otherIds = userFriendIds.get(otherId);
        if (userIds == null || otherIds == null) return null;
        List<Long> mutualIds = new ArrayList<>();
        for (Long idMutual : userIds) {
            if (otherIds.contains(idMutual)) mutualIds.add(idMutual);
        }
        return new ArrayList<>(mutualIds);
    }

    private void addFriendId (Long userId, Long otherId) {
        Set<Long> friends;
        if (userFriendIds.containsKey(userId)) {
            friends = new HashSet<>(userFriendIds.get(userId));
            friends.add(otherId);
        } else {
                friends = new HashSet<>();
                friends.add(otherId);
            }
        userFriendIds.put(userId, friends);
        }

    private void deleteFriendId(Long userId, Long otherId) {
        Set<Long> friends;
        friends = new HashSet<>(userFriendIds.get(userId));
        friends.remove(otherId);
        userFriendIds.put(userId, friends);
    }

}
