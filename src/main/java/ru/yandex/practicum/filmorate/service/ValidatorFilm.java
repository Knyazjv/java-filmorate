package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
 public class ValidatorFilm {
    private static final LocalDate BEGINNING_OF_CINEMA = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final int MAX_LENGTH_DESCRIPTION = 200;

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() >= MAX_LENGTH_DESCRIPTION) {
            log.warn("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate() == null) {
            log.warn("Отсутствует дата релиза");
            throw new ValidationException("Отсутствует дата релиза");
        } else if (film.getReleaseDate().isBefore(BEGINNING_OF_CINEMA)) {
            log.warn("Дата релиза не должна быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
