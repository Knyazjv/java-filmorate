package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorFilmTest {
    @Test
    void shouldThrowExceptionNameFilmIsEmpty() {
        Film film = new Film(1L, "", "description", LocalDate.now(), 123);
        assertEquals("Название не может быть пустым", validation(film).getMessage());
    }

    @Test
    void shouldThrowExceptionNameFilmIsNull() {
        Film film = new Film(1L, null, "description", LocalDate.now(), 123);
        assertEquals("Название не может быть пустым", validation(film).getMessage());
    }

    @Test
    void shouldThrowExceptionDescriptionLength201() {
        String description = "DLaeCKMpYbgqpUVaDkguZTrEGyaimOCbwpQodQiBqvMQpBCVugmXRgbiIVH" +
                "VJPtHWLGhuZKfPfbtIGRFvAkhXycnnFqKaXhYGKAKyWnThsTiOWIqOyjVnMehiVAfolCXCvdhkN" +
                "okNZjUNaQuUwnEVOAEEotgAZeOvJVykxFiHrTPirvPKJFRepmApdMBWOuxpLQEYZVQ";
        Film film = new Film(1L, "name", description, LocalDate.now(), 123);
        assertEquals("Максимальная длина описания — 200 символов", validation(film).getMessage());
    }

    @Test
    void descriptionLength200() {
        String description = "DLaeCKMpYbgqpUVaDkguZTrEGyaimOCbwpQodQiBqvMQpBCVugmXRgbiIVH" +
                "VJPtHWLGhuZKfPfbtIGRFvAkhXycnnFqKaXhYGKAKyWnThsTiOWIqOyjVnMehiVAfolCXCvdhkN" +
                "okNZjUNaQuUwnEVOAEEotgAZeOvJVykxFiHrTPirvPKJFRepmApdMBWOuxpLQEYZV";
        Film film = new Film(1L, "name", description, LocalDate.now(), 123);
        ValidatorFilm.validateFilm(film);
    }

    @Test
    void shouldThrowExceptionReleaseDateBeforeBeginningOfCinema() {
        Film film = new Film(1L, "name", "description", LocalDate.of(1895, Month.DECEMBER,
                27), 123);
        assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года", validation(film).getMessage());
    }

    @Test
    void releaseDateAfterBeginningOfCinema() {
        Film film = new Film(1L, "name", "description", LocalDate.of(1895, Month.DECEMBER,
                29), 123);
        ValidatorFilm.validateFilm(film);
    }

    @Test
    void shouldThrowExceptionDurationIsNegative() {
        Film film = new Film(1L, "name", "description", LocalDate.now(), 0);
        assertEquals("Продолжительность фильма должна быть положительной", validation(film).getMessage());
        Film film1 = new Film(1L, "name", "description", LocalDate.now(), -1);
        assertEquals("Продолжительность фильма должна быть положительной", validation(film1).getMessage());
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