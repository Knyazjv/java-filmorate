package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Repository
public class FilmRepositoryImpl implements FilmRepository {

    private Integer idFilm = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(idFilm);
        films.put(idFilm++, film);
        log.info("Фильм успешно добавлен");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if(films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлен");
            return film;
        }
        throw new ValidationException("Фильм не найден");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
