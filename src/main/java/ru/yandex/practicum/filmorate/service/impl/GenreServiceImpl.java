package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreRepository;

    @Autowired
    public GenreServiceImpl(GenreStorage genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAllGenre() {
        return genreRepository.getAllGenre();
    }

    @Override
    public Genre getGenreById(Long genreId) {
        Genre genre = genreRepository.getGenreById(genreId);
        if (genre == null) {
            log.warn("Error");
            log.warn("genreId: " + genreId + " жанр не найден.");
            throw new NotFoundException("Жанр не найден.");
        }
        return genreRepository.getGenreById(genreId);
    }
}
