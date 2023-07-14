package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    List<Film> getFilms();

    Film getFilmById(Long filmId);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getPopularFilm(Integer count);
}
