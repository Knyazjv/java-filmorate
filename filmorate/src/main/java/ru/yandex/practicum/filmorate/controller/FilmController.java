package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    private Film add(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    private Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films")
    private List<Film> getFilms() {
        return filmService.getFilms();
    }

}
