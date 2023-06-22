package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidatorUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorUserTest {
    private final String EMAIL_CANNOT_BE_EMPTY = "Электронная почта не может быть пустой и должна содержать символ @";
    private final String LOGIN_CANNOT_BE_EMPTY = "Логин не может быть пустым и содержать пробелы";
    private final String BIRTHDAY_CANNOT_BE_IN_THE_FUTURE = "Дата рождения не может быть в будущем";

    @Test
    void shouldThrowExceptionEmailIsEmptyAndContainAt() {
        User user = new User(1L, "", "login", "name", LocalDate.now());
        assertEquals(EMAIL_CANNOT_BE_EMPTY, validation(user).getMessage());
        User user1 = new User(1L, "mailmailmail", "login", "name", LocalDate.now());
        assertEquals(EMAIL_CANNOT_BE_EMPTY, validation(user1).getMessage());
    }

    @Test
    void correctEmail() {
        User user = new User(1L, "mailmailmail@yandex.ru", "login", "name", LocalDate.now());
        ValidatorUser.validateUser(user);
    }

    @Test
    void shouldThrowExceptionLoginIsEmptyAndContainSpaces() {
        User user = new User(1L, "mail@yandex.ru", "", "name", LocalDate.now());
        assertEquals(LOGIN_CANNOT_BE_EMPTY, validation(user).getMessage());
        User user1 = new User(1L, "mail@yandex.ru", "login login", "name", LocalDate.now());
        assertEquals(LOGIN_CANNOT_BE_EMPTY, validation(user1).getMessage());
    }

    @Test
    void correctLogin() {
        User user = new User(1L, "mail@yandex.ru", "login", "name", LocalDate.now());
        ValidatorUser.validateUser(user);
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
        assertEquals(BIRTHDAY_CANNOT_BE_IN_THE_FUTURE, validation(user).getMessage());
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
