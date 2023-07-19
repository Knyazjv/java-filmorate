package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(GenreDBStorage.class)
class GenreDBStorageTest {

    @Autowired
    private GenreStorage genreStorage;
    @Test
    void getAllGenre() {
        List<String> names = new ArrayList<>();
        names.add("Комедия");
        names.add("Драма");
        names.add("Мультфильм");
        names.add("Триллер");
        names.add("Документальный");
        names.add("Боевик");
        List<Genre> genreList =genreStorage.getAllGenre();
        for (int i = 0; i < names.size(); i++) {
            equalsGenre(i + 1, names.get(i), genreList.get(i));
        }
    }

    @Test
    void getGenreById() {
        Genre genre = genreStorage.getGenreById(3L);
        Genre genre1 = genreStorage.getGenreById(5L);
        equalsGenre(3, "Мультфильм", genre);
        equalsGenre(5, "Документальный", genre1);
    }

    private void equalsGenre(int genreId, String nameGenre, Genre genre) {
        assertEquals(genreId, genre.getId());
        assertEquals(nameGenre, genre.getName());
    }
}