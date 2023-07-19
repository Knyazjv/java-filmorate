package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidatorFilm;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;


    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film create(Film film) {
        ValidatorFilm.validateFilm(film);
        validateGenre(film.getGenres());
        validateMpa(film.getMpa().getId());
        return filmStorage.createFilm(film);
    }

    @Override
    public Film update(Film film) {
        ValidatorFilm.validateFilm(film);
        getFilmById(film.getId());
        validateGenre(film.getGenres());
        validateMpa(film.getMpa().getId());
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) throw new NotFoundException("Фильм по id: " + filmId + " не найден");
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
        return filmStorage.getPopularFilm(count);
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

    private void validateGenre(Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) return;
        List<Long> genreIds = genres.stream().map(Genre::getId).collect(Collectors.toList());
        List<Long> baseGenreIds = genreStorage.getAllGenre().stream().map(Genre::getId).collect(Collectors.toList());
        for (Long genreId : genreIds) {
            if(!baseGenreIds.contains(genreId)) {
                log.warn("Error");
                log.warn("genreId= " + genreId + " not found");
                throw new NotFoundException("Жанр не найден");
            }
        }
    }

    private void validateMpa(Long mpaId) {
        List<Mpa> mpaList = mpaStorage.getAllMpa();
        for (Mpa mpa : mpaList) {
            if(mpa.getId() == mpaId) return;
        }
        log.warn("Error");
        log.warn("genreId= " + mpaId + " not found");
        throw new NotFoundException("Рэйтинг не найден");
    }
}
