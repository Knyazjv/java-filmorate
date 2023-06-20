package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidatorFilm;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public InMemoryFilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film create(Film film) {
        ValidatorFilm.validateFilm(film);
        return filmStorage.createFilm(film);
    }

    @Override
    public Film update(Film film) {
        ValidatorFilm.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public void putLike(Long filmId, Long userId) {
        validId(filmId, userId);
        filmStorage.putLike(filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        validId(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        List<Film> popularFilms = new ArrayList<>();
        List<Long> idPopularFilms = filmStorage.getPopularFilm(count);
        if (idPopularFilms.isEmpty()) return popularFilms;
        for (Long idFilm : idPopularFilms) {
            popularFilms.add(filmStorage.getFilmById(idFilm));
        }
        return popularFilms;
    }

    private void validId(Long filmId, Long userId) {
        if (userStorage.getUserById(userId) == null) throw new NotFoundException("Пользователь отсутствует");
        if (filmStorage.getFilmById(filmId) == null) throw new NotFoundException("Отсутствует фильм");
    }
}
