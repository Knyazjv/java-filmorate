package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Repository
public class MpaDBStorage implements MpaStorage {

    private final NamedParameterJdbcOperations jdbcOperations;

    public MpaDBStorage(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Mpa> getAllMpa() {
        final String sqlQuery = "select MPA_ID, NAME_MPA from MPA";
        return jdbcOperations.query(sqlQuery, new MpaRowMapper());
    }

    @Override
    public Mpa getMpaById(Long mpaId) {
        final String sqlQuery = "select MPA_ID, NAME_MPA from MPA where MPA_ID = :mpaId";
        final List<Mpa> mpas = jdbcOperations.query(sqlQuery,
                Map.of("mpaId", mpaId), new MpaRowMapper());
        if (mpas.size() != 1) return null;
        return mpas.get(0);
    }

    private static class MpaRowMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Mpa(rs.getLong("MPA_ID"), rs.getString("NAME_MPA"));
        }
    }
}
