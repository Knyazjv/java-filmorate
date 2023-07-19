package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDBStorage implements GenreStorage {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final GenreRowMapper genreRowMapper = new GenreRowMapper();

    public GenreDBStorage(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Genre> getAllGenre() {
        final String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRE";
        return jdbcOperations.query(sqlQuery, genreRowMapper);
    }

    @Override
    public Genre getGenreById(Long genreId) {
        final String sqlGenreQuery = "select GENRE_ID, GENRE_NAME " +
                "from GENRE " +
                "where GENRE_ID = :genreId";
        final List<Genre> genres = jdbcOperations.query(sqlGenreQuery,
                Map.of("genreId", genreId), genreRowMapper);
        if (genres.size() != 1) return null;
        return genres.get(0);
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getLong("GENRE_ID"),
                    rs.getString("GENRE_NAME"));
        }
    }
}
