package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.ValidatorFilm;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorFilmTest {
    private static final String THE_NAME_CANNOT_BE_EMPTY = "Название не может быть пустым";
    private static final String DURATION_OF_THE_FILM_IS_POSITIVE = "Продолжительность фильма должна быть положительной";
    private static final String DATE_BEFORE_BEGINNING_OF_CINEMA = "Дата релиза не должна быть раньше 28 декабря 1895 года";
    private static final String MAX_DESCRIPTION = "Максимальная длина описания — 200 символов";
    private static final String DESCRIPTION_LENGTH_200 = "DLaeCKMpYbgqpUVaDkguZTrEGyaimOCbwpQodQiBqvMQpBCVugmXRgbiIVH" +
            "VJPtHWLGhuZKfPfbtIGRFvAkhXycnnFqKaXhYGKAKyWnThsTiOWIqOyjVnMehiVAfolCXCvdhkN" +
            "okNZjUNaQuUwnEVOAEEotgAZeOvJVykxFiHrTPirvPKJFRepmApdMBWOuxpLQEYZV";

    @Test
    void shouldThrowExceptionNameFilmIsEmpty() {
        Film film = new Film(1L, "", "description", LocalDate.now(), 123, new Mpa(1, "name"), new HashSet<>());
        assertEquals(THE_NAME_CANNOT_BE_EMPTY, validation(film).getMessage());
    }

    @Test
    void shouldThrowExceptionNameFilmIsNull() {
        Film film = new Film(1L, null, "description", LocalDate.now(), 123, new Mpa(1, "name"), new HashSet<>());
        assertEquals(THE_NAME_CANNOT_BE_EMPTY, validation(film).getMessage());
    }

    @Test
    void nameFilmIsNotNullAndEmpty() {
        Film film = new Film(1L, "name", "description", LocalDate.now(), 123, new Mpa(1, "name"), new HashSet<>());
        ValidatorFilm.validateFilm(film);
    }

    @Test
    void shouldThrowExceptionDescriptionLength201() {
        Film film = new Film(1L, "name", DESCRIPTION_LENGTH_200 + "1", LocalDate.now(), 123, new Mpa(1, "name"), new HashSet<>());
        assertEquals(MAX_DESCRIPTION, validation(film).getMessage());
    }

    @Test
    void descriptionLength200() {
        Film film = new Film(1L, "name", DESCRIPTION_LENGTH_200, LocalDate.now(), 123, new Mpa(1, "name"), new HashSet<>());
        ValidatorFilm.validateFilm(film);
    }

    @Test
    void shouldThrowExceptionReleaseDateBeforeBeginningOfCinema() {
        Film film = new Film(1L, "name", "description", LocalDate.of(1895, Month.DECEMBER,
                27), 123, new Mpa(1, "name"), new HashSet<>());
        assertEquals(DATE_BEFORE_BEGINNING_OF_CINEMA, validation(film).getMessage());
    }

    @Test
    void releaseDateAfterBeginningOfCinema() {
        Film film = new Film(1L, "name", "description", LocalDate.of(1895, Month.DECEMBER,
                29), 123, new Mpa(1, "name"), new HashSet<>());
        ValidatorFilm.validateFilm(film);
    }

    @Test
    void shouldThrowExceptionDurationIsNegative() {
        Film film = new Film(1L, "name", "description", LocalDate.now(), 0, new Mpa(1, "name"), new HashSet<>());
        assertEquals(DURATION_OF_THE_FILM_IS_POSITIVE, validation(film).getMessage());
        Film film1 = new Film(1L, "name", "description", LocalDate.now(), -1, new Mpa(1, "name"), new HashSet<>());
        assertEquals(DURATION_OF_THE_FILM_IS_POSITIVE, validation(film1).getMessage());
    }

    @Test
    void durationIsPositive() {
        Film film = new Film(1L, "name", "description", LocalDate.now(), 2, new Mpa(1, "name"), new HashSet<>());
        ValidatorFilm.validateFilm(film);
    }

    private ValidationException validation(Film film) {
        return assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        ValidatorFilm.validateFilm(film);
                    }
                });
    }
}