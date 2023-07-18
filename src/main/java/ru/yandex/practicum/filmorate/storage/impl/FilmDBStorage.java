package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDBStorage implements FilmStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    public FilmDBStorage(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values (:name, :description, :releaseDate, :duration, :mpaId);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(sqlQuery, getMapParameter(film, true), keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        setFilmGenre(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()) == null) return null;
        String sqlQuery = "update FILMS SET FILM_NAME = :name, DESCRIPTION = :description, " +
                "RELEASE_DATE = :releaseDate, DURATION = :duration, MPA_ID = :mpaId " +
                "where FILM_ID = :filmId";
        jdbcOperations.update(sqlQuery, getMapParameter(film, false));
        removeGenres(film.getId());
        setFilmGenre(film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA_ID, MPA.NAME_MPA " +
                "from FILMS as F " +
                "left join MPA on F.MPA_ID = MPA.MPA_ID";
        List<Film> films = jdbcOperations.query(sqlQuery, new FilmRowMapper());
        loadFilmsGenre(films);
        return films;
    }

    @Override
    public Film getFilmById(Long filmId) {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, " +
                "F.RELEASE_DATE, F.DURATION, F.MPA_ID, MPA.NAME_MPA " +
                "from FILMS as F " +
                "left join MPA on F.MPA_ID = MPA.MPA_ID " +
                "where F.FILM_ID = :filmId";
        final List<Film> films = jdbcOperations.query(sqlQuery,
                Map.of("filmId", filmId), new FilmRowMapper());
        if (films.size() != 1) return null;
        final String sqlGenreQuery = "select G.GENRE_ID, G.GENRE_NAME " +
                "from GENRE as G " +
                "right join FILM_GENRE as FG on FG.GENRE_ID = G.GENRE_ID " +
                "where FG.FILM_ID = :filmId";
        final List<Genre> genres = jdbcOperations.query(sqlGenreQuery,
                Map.of("filmId", filmId), new GenreRowMapper());
        Film film = films.get(0);
        film.setGenres(new TreeSet<>((o1, o2) -> (int) (o1.getId() - o2.getId())));
        film.getGenres().addAll(genres);
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "insert into FILM_LIKE (FILM_ID, USER_ID) " +
                "values (:filmId, :userId)";
        jdbcOperations.update(sqlQuery, getLikeMapParameter(filmId, userId));
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        final String sqlQuery = "delete from FILM_LIKE " +
                "where FILM_ID = :filmId and USER_ID = :userId";
        jdbcOperations.update(sqlQuery, getLikeMapParameter(filmId, userId));
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, " +
                "F.DURATION, F.MPA_ID, MPA.NAME_MPA " +
                "from FILMS as F " +
                "left join FILM_LIKE as FL on F.FILM_ID = FL.FILM_ID " +
                "left join MPA on F.MPA_ID = MPA.MPA_ID " +
                "group by F.FILM_ID  " +
                "order by COUNT(FL.USER_ID) desc limit :count";
        List<Film> films = jdbcOperations.query(sqlQuery, Map.of("count", count), new FilmRowMapper());
        loadFilmsGenre(films);
        return films;
    }

    private MapSqlParameterSource getMapParameter(Film film, boolean isCreate) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("releaseDate", film.getReleaseDate());
        map.addValue("duration", film.getDuration());
        map.addValue("mpaId", film.getMpa().getId());
        if(!isCreate) map.addValue("filmId", film.getId());
        return map;
    }

    private void setFilmGenre(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) return;
        TreeSet<Genre> genreTreeSet = new TreeSet<>((o1, o2) -> (int) (o1.getId() - o2.getId()));
        for (Genre genre : film.getGenres()) {
            String sqlQueryAddGenre = "insert into FILM_GENRE (GENRE_ID, FILM_ID) " +
                    "values (:genreId, :filmId)";
            jdbcOperations.update(sqlQueryAddGenre, getMapGenreParameter(film, genre));
            genreTreeSet.add(genre);
        }
        film.setGenres(genreTreeSet);
    }

    private MapSqlParameterSource getMapGenreParameter(Film film, Genre genre) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("genreId", genre.getId());
        map.addValue("filmId", film.getId());
        return map;
    }

    private void loadFilmsGenre(List<Film> films) {
        List<Long> filmsId = films.stream().map(Film::getId).collect(Collectors.toList());
        final Map<Long, Film> filmMap = films.stream()
				.collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b));
        final String sqlQuery = "select FG.FILM_ID, FG.GENRE_ID, G.GENRE_NAME " +
                "FROM FILM_GENRE as FG " +
                "left join GENRE as G on FG.GENRE_ID = G.GENRE_ID " +
                "WHERE film_id in (:filmsId)";
        final List<FilmGenre> genreMaps = jdbcOperations.query(sqlQuery,
                Map.of("filmsId", filmsId), new FilmGenreRowMapper());
        for (FilmGenre filmGenre : genreMaps) {
            filmMap.get(filmGenre.getFilmId()).getGenres().add(filmGenre.getGenre());
        }
    }

    private void removeGenres(Long filmId) {
        final String sqlQuery = "delete from FILM_GENRE " +
                "where FILM_ID = :filmId";
        jdbcOperations.update(sqlQuery, Map.of("filmId", filmId));
    }

    private MapSqlParameterSource getLikeMapParameter(Long filmId, Long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        map.addValue("userId", userId);
        return map;
    }

    @Data
    private static class FilmGenre {
        final long filmId;
        final Genre genre;
    }

    private static class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Film(rs.getLong("FILM_ID"),
                    rs.getString("FILM_NAME"),
                    rs.getString("DESCRIPTION"),
                    rs.getDate("RELEASE_DATE").toLocalDate(),
                    rs.getInt("DURATION"),
                    new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA.NAME_MPA")),
                    new TreeSet<>((o1, o2) -> (int) (o1.getId() - o2.getId())));
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getLong("GENRE_ID"),
                    rs.getString("GENRE_NAME"));
        }
    }

    private  static class FilmGenreRowMapper implements RowMapper<FilmGenre> {
        @Override
        public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FilmGenre(rs.getLong("FILM_ID"),
                    new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME")));
        }
    }
}
