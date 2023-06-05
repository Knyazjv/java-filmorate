package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidatorFilm;

import java.util.List;


@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Film create(Film film) {
        ValidatorFilm.validateFilm(film);
        return filmRepository.createFilm(film);
    }

    @Override
    public Film update(Film film) {
        ValidatorFilm.validateFilm(film);
        return filmRepository.updateFilm(film);
    }

    @Override
    public List<Film> getFilms() {
        return filmRepository.getAllFilms();
    }
}
