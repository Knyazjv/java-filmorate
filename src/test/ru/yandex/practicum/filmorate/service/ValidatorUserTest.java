package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorUserTest {

    @Test
    void shouldThrowExceptionEmailIsEmptyAndContainAt() {
        User user = new User(1L, "", "login", "name", LocalDate.now());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", validation(user).getMessage());
        User user1 = new User(1L, "mailmailmail", "login", "name", LocalDate.now());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", validation(user1).getMessage());
    }

    @Test
    void shouldThrowExceptionLoginIsEmptyAndContainSpaces() {
        User user = new User(1L, "mail@yandex.ru", "", "name", LocalDate.now());
        assertEquals("Логин не может быть пустым и содержать пробелы", validation(user).getMessage());
        User user1 = new User(1L, "mail@yandex.ru", "login login", "name", LocalDate.now());
        assertEquals("Логин не может быть пустым и содержать пробелы", validation(user1).getMessage());
    }

    @Test
    void ifTheNameIsEmpty() {
        User user = new User(1L, "mail@yandex.ru", "login", "", LocalDate.now());
        ValidatorUser.validateUser(user);
        assertEquals("login", user.getName());
        User user1 = new User(1L, "mail@yandex.ru", "login", null, LocalDate.now());
        ValidatorUser.validateUser(user1);
        assertEquals("login", user1.getName());
    }

    @Test
    void shouldThrowExceptionBirthdayAfterNow() {
        User user = new User(1L, "mail@yandex.ru", "login", "name", LocalDate.now().plusDays(1));
        assertEquals("Дата рождения не может быть в будущем", validation(user).getMessage());
        User user1 = new User(1L, "mail@yandex.ru", "login", "name", LocalDate.now());
        ValidatorUser.validateUser(user1);
    }

    private ValidationException validation(User user) {
        return assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        ValidatorUser.validateUser(user);
                    }
                });
    }
}
