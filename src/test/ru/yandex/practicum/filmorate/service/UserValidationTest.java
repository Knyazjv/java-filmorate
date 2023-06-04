package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Test
    void shouldThrowExceptionEmailIsEmptyAndContainAt() {
        User user = new User(1, "", "login", "name", LocalDate.now());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", validation(user).getMessage());
        User user1 = new User(1, "mailmailmail", "login", "name", LocalDate.now());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", validation(user1).getMessage());
    }

    @Test
    void shouldThrowExceptionLoginIsEmptyAndContainSpaces() {
        User user = new User(1, "mail@yandex.ru", "", "name", LocalDate.now());
        assertEquals("Логин не может быть пустым и содержать пробелы", validation(user).getMessage());
        User user1 = new User(1, "mail@yandex.ru", "login login", "name", LocalDate.now());
        assertEquals("Логин не может быть пустым и содержать пробелы", validation(user1).getMessage());
    }

    @Test
    void ifTheNameIsEmpty() {
        User user = new User(1, "mail@yandex.ru", "login", "", LocalDate.now());
        UserValidation.validationUser(user);
        assertEquals("login", user.getName());
        User user1 = new User(1, "mail@yandex.ru", "login", null, LocalDate.now());
        UserValidation.validationUser(user1);
        assertEquals("login", user1.getName());
    }

    @Test
    void shouldThrowExceptionBirthdayAfterNow() {
        User user = new User(1, "mail@yandex.ru", "login", "name", LocalDate.now().plusDays(1));
        assertEquals("Дата рождения не может быть в будущем", validation(user).getMessage());
        User user1 = new User(1, "mail@yandex.ru", "login", "name", LocalDate.now());
        UserValidation.validationUser(user1);
    }

    private ValidationException validation(User user) {
        return assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        UserValidation.validationUser(user);
                    }
                });
    }
}

/*Для User:
электронная почта не может быть пустой и должна содержать символ @;
логин не может быть пустым и содержать пробелы;
имя для отображения может быть пустым — в таком случае будет использован логин;
дата рождения не может быть в будущем.*/