package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private Long idFilm = 1L;
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(idFilm);
        likes.put(idFilm, new HashSet<>());
        films.put(idFilm++, film);
        log.info("Фильм успешно добавлен");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлен");
            return film;
        }
        throw new NotFoundException("Фильм не найден");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (films.containsKey(filmId)) return films.get(filmId);
        return null;
    }

    @Override
    public void putLike(Long filmId, Long userId) {
        Set<Long> filmLikes = new HashSet<>(likes.get(filmId));
        filmLikes.add(userId);
        likes.put(filmId, filmLikes);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        Set<Long> filmLikes = new HashSet<>(likes.get(filmId));
        filmLikes.remove(userId);
        likes.put(filmId, filmLikes);
    }

    @Override
    public List<Long> getPopularFilm(Integer count) {
        List<Long> result = new ArrayList<>();
        likes.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Set<Long>>comparingByValue(Comparator.comparingInt(Set::size)).reversed())
                .limit(count)
                .forEach(entry -> result.add(entry.getKey()));
        return result;
    }
}
