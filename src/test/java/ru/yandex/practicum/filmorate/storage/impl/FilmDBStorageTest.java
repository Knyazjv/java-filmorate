package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(FilmDBStorage.class)
class FilmDBStorageTest {

    @Autowired
    private FilmStorage filmStorage;
    @Test
    void testCreateFilm() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(3,null));
        Film film = new Film(0L, "testName", "testDescription",
                LocalDate.of(2000, 1, 1),
                100, new Mpa(2, null), genres);
        Film film2 = filmStorage.createFilm(film);
        equalsFilm(5, "testName", "testDescription", LocalDate.of(2000, 1, 1),
                100, 2, film2);
        equalsGenre(3, genres);
        Film film3 = filmStorage.getFilmById(5L);
        equalsFilm(5, "testName", "testDescription", LocalDate.of(2000, 1, 1),
                100, 2, film3);
        equalsGenre(3, genres);
    }

    @Test
    void testUpdateFilm() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(2,null));
        Film film = filmStorage.getFilmById(2L);
        equalsFilm(2, "StarWars", "description2", LocalDate.of(1977, 5, 25),
                121, 5, film);
        equalsGenre(2, genres);
        film = new Film(2L, "testName", "testDescription",
                LocalDate.of(2000, 1, 1),
                100, new Mpa(2, null), genres);
        filmStorage.updateFilm(film);
        equalsFilm(2, "testName", "testDescription", LocalDate.of(2000, 1, 1),
                100, 2, film);
        equalsGenre(2, genres);
    }

    @Test
    void testGetAllFilms() {
        List<Film> filmList = filmStorage.getAllFilms();
        equalsFilm(1, "Titanic", "description1", LocalDate.of(1997, 12, 9),
                194, 4, filmList.get(0));
        equalsFilm(2, "StarWars", "description2", LocalDate.of(1977, 5, 25),
                121, 5, filmList.get(1));
        equalsFilm(3, "Avengers", "description3", LocalDate.of(2019, 4, 26),
                142, 3, filmList.get(2));
        equalsFilm(4, "The Dark Knight", "description4", LocalDate.of(2008, 7, 18),
                152, 5, filmList.get(3));
    }

    @Test
    void testGetFilmById() {
        Film film =  filmStorage.getFilmById(3L);
        equalsFilm(3, "Avengers", "description3", LocalDate.of(2019, 4, 26),
                142, 3, film);
        film =  filmStorage.getFilmById(4L);
        equalsFilm(4, "The Dark Knight", "description4", LocalDate.of(2008, 7, 18),
                152, 5, film);
    }

    @Test
    void testAddLikeAndGetPopularFilm() {
        List<Film> films = filmStorage.getPopularFilm(4);
        equalsFilm(1, "Titanic", "description1", LocalDate.of(1997, 12, 9),
                194, 4, films.get(0));
        equalsFilm(2, "StarWars", "description2", LocalDate.of(1977, 5, 25),
                121, 5, films.get(1));
        filmStorage.addLike(1L, 1L);
        List<Film> films1 = filmStorage.getPopularFilm(4);
        equalsFilm(1, "Titanic", "description1", LocalDate.of(1997, 12, 9),
                194, 4, films1.get(0));
        equalsFilm(2, "StarWars", "description2", LocalDate.of(1977, 5, 25),
                121, 5, films1.get(1));
        filmStorage.addLike(2L, 1L);
        filmStorage.addLike(2L, 2L);
        filmStorage.addLike(3L, 1L);
        filmStorage.addLike(3L, 2L);
        filmStorage.addLike(3L, 3L);
        List<Film> films2 = filmStorage.getPopularFilm(4);
        equalsFilm(3, "Avengers", "description3", LocalDate.of(2019, 4, 26),
                142, 3, films2.get(0));
        equalsFilm(2, "StarWars", "description2", LocalDate.of(1977, 5, 25),
                121, 5, films2.get(1));
        equalsFilm(1, "Titanic", "description1", LocalDate.of(1997, 12, 9),
                194, 4, films2.get(2));
    }

    @Test
    void testRemoveLike() {
        filmStorage.addLike(2L, 1L);
        filmStorage.addLike(2L, 2L);
        filmStorage.addLike(3L, 1L);
        filmStorage.addLike(3L, 2L);
        filmStorage.addLike(3L, 3L);
        List<Film> films = filmStorage.getPopularFilm(2);
        equalsFilm(3, "Avengers", "description3", LocalDate.of(2019, 4, 26),
                142, 3, films.get(0));
        equalsFilm(2, "StarWars", "description2", LocalDate.of(1977, 5, 25),
                121, 5, films.get(1));
        filmStorage.removeLike(3L, 1L);
        filmStorage.removeLike(3L, 2L);
        List<Film> films1 = filmStorage.getPopularFilm(2);
        equalsFilm(2, "StarWars", "description2", LocalDate.of(1977, 5, 25),
                121, 5, films1.get(0));
        equalsFilm(3, "Avengers", "description3", LocalDate.of(2019, 4, 26),
                142, 3, films1.get(1));
    }

    private void equalsGenre(int genreId, Set<Genre> genres) {
        Genre[] arrayGenres = genres.toArray(new Genre[0]);
        assertEquals(genreId, arrayGenres[0].getId());
    }

    private void equalsFilm(long id, String name, String description,
                            LocalDate releaseDate, int duration, int mpaId, Film film) {
        assertEquals(id, film.getId());
        assertEquals(name, film.getName());
        assertEquals(description, film.getDescription());
        assertEquals(releaseDate, film.getReleaseDate());
        assertEquals(duration, film.getDuration());
        assertEquals(mpaId, film.getMpa().getId());
    }
}