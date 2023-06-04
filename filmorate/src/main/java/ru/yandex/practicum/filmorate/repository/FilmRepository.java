package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    List<Film> getAllFilms();
}
