package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(Long filmId);

    void putLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Long> getPopularFilm(Integer count);


}
