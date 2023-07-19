package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(UserDBStorage.class)
class UserDBStorageTest {
    @Autowired
    private UserStorage userStorage;
    @Test
    void testCreateUser() {
        User user = new User(0L, "emailTest@mail.ru", "loginTest",
                "nameTest", LocalDate.of(2000, 1, 1));
        userStorage.createUser(user);
        User userTest = userStorage.getUserById(5L);
        equalsUser(5L, "emailTest@mail.ru", "loginTest",
                "nameTest", LocalDate.of(2000, 1, 1), userTest);
    }

    @Test
    void testUpdateUser() {
        User user = new User(2L, "emailTest@mail.ru", "loginTest",
                "nameTest", LocalDate.of(2000, 1, 1));
        User userTest = userStorage.getUserById(2L);
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), userTest);
        userStorage.updateUser(user);
        User userTest2 = userStorage.getUserById(2L);
        equalsUser(2L, "emailTest@mail.ru", "loginTest",
                "nameTest", LocalDate.of(2000, 1, 1), userTest2);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userStorage.getAllUsers();
        equalsUser(1L, "email@email.com", "superUser",
                "name1", LocalDate.of(1997, 9, 20), users.get(0));
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), users.get(1));
        equalsUser(3L, "mail@mail.ru", "userTest",
                "name3", LocalDate.of(2000, 7, 21), users.get(2));
    }

    @Test
    void testGetUserById() {
        User userTest = userStorage.getUserById(2L);
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), userTest);
    }

    @Test
    void testAddFriendAndGet() {
        List<User> users = userStorage.getFriends(1L);
        assertEquals(users.size(), 0);
        List<User> users2 = userStorage.getFriends(3L);
        assertEquals(users2.size(), 0);
        userStorage.addFriend(1L, 3L);
        List<User> users3 = userStorage.getFriends(1L);
        assertEquals(users3.size(), 1);
        equalsUser(3L, "mail@mail.ru", "userTest",
                "name3", LocalDate.of(2000, 7, 21), users3.get(0));
        userStorage.addFriend(1L, 2L);
        List<User> users4 = userStorage.getFriends(1L);
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), users4.get(0));
        equalsUser(3L, "mail@mail.ru", "userTest",
                "name3", LocalDate.of(2000, 7, 21), users4.get(1));
        assertEquals(users4.size(), 2);
    }

    @Test
    void deleteFriend() {
        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(1L, 4L);
        List<User> users = userStorage.getFriends(1L);
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), users.get(0));
        equalsUser(3L, "mail@mail.ru", "userTest",
                "name3", LocalDate.of(2000, 7, 21), users.get(1));
        equalsUser(4L, "gmail@gmail.com", "superMan",
                "name4", LocalDate.of(1981, 5, 14), users.get(2));
        userStorage.deleteFriend(1L, 3L);
        List<User> users2 = userStorage.getFriends(1L);
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), users2.get(0));
        equalsUser(4L, "gmail@gmail.com", "superMan",
                "name4", LocalDate.of(1981, 5, 14), users2.get(1));
    }

    @Test
    void testGetListOfMutualFriends() {
        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(1L, 4L);
        List<User> users = userStorage.getListOfMutualFriends(1L, 3L);
        assertEquals(users.size(), 0);
        userStorage.addFriend(3L, 1L);
        userStorage.addFriend(3L, 2L);
        List<User> users2 = userStorage.getListOfMutualFriends(1L, 3L);
        assertEquals(users2.size(), 1);
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), users2.get(0));
        userStorage.addFriend(3L, 4L);
        List<User> users3 = userStorage.getListOfMutualFriends(1L, 3L);
        assertEquals(users3.size(), 2);
        equalsUser(2L, "ya@ya.ru", "user",
                "name2", LocalDate.of(2002, 3, 24), users3.get(0));
        equalsUser(4L, "gmail@gmail.com", "superMan",
                "name4", LocalDate.of(1981, 5, 14), users3.get(1));
    }

    private void equalsUser(Long id, String email, String login,
                            String name, LocalDate birthday, User user) {
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(login, user.getLogin());
        assertEquals(name, user.getName());
        assertEquals(birthday, user.getBirthday());

    }
}