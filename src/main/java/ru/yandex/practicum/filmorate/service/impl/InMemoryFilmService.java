package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidatorFilm;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
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
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) throw new NotFoundException("Фильм по id: " + filmId + "не найден");
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        validatorId(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        validatorId(filmId, userId);
        filmStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        return filmStorage.getPopularFilm(count).stream()
                .map(filmStorage::getFilmById)
                .collect(Collectors.toList());
    }

    private void validatorId(Long filmId, Long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.warn("Error");
            log.warn("userId: " + userId);
            throw new NotFoundException("Пользователь отсутствует");
        }
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            log.warn("Error");
            log.warn("filmId: " + filmId);
            throw new NotFoundException("Отсутствует фильм");
        }
    }
}
