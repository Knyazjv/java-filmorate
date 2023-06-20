package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final int DEFAULT_COUNT_FILM = 10;

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    private Film add(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    private Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping
    private List<Film> getFilms() {
        return filmService.getFilms();
    }

   @GetMapping(value = "/{id}")
    private ResponseEntity<?> getFilmById(@PathVariable(value = "id") Long idFilm) {
       Film film = filmService.getFilmById(idFilm);
       if (film == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       return new ResponseEntity<>(film, HttpStatus.OK);
   }

   @PutMapping(value = "/{id}/like/{userId}")
    private void putLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.putLike(filmId,userId);
   }

   @DeleteMapping(value = "{id}/like/{userId}")
   private void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
       filmService.deleteLike(filmId,userId);
   }

   @GetMapping(value = "/popular")
    private List<Film> getPopularFilm(@RequestParam(required = false, value = "count") Optional<Integer> count) {
        if (count.isEmpty() || count.get() <= 0) return filmService.getPopularFilm(DEFAULT_COUNT_FILM);
        return filmService.getPopularFilm(count.get());
   }
}
