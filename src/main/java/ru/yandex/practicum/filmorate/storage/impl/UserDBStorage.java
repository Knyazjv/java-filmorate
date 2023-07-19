package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserDBStorage implements UserStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    public UserDBStorage(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "values (:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(sqlQuery, getMapParameter(user, true), keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserById(user.getId()) == null) return null;
        String sqlQuery = "update USERS SET EMAIL = :email, LOGIN = :login, USER_NAME = :name, BIRTHDAY = :birthday " +
                "where USER_ID = :userId";
        jdbcOperations.update(sqlQuery, getMapParameter(user, false));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        final String sqlQuery = "select USER_ID, LOGIN, BIRTHDAY, EMAIL, USER_NAME from USERS";
        return jdbcOperations.query(sqlQuery, new UserRowMapper());
    }

    @Override
    public User getUserById(Long userId) {
        final String sqlQuery = "select USER_ID, LOGIN, BIRTHDAY, EMAIL, USER_NAME " +
                "from USERS " +
                "where USER_ID = :userId ";
        final List<User> users = jdbcOperations.query(sqlQuery, Map.of("userId", userId), new UserRowMapper());

        if (users.size() != 1) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        final String sqlQuery = "insert into STATUS_FRIEND (USER_ID, FRIEND_ID) " +
                "values (:userId, :friendId)";
        jdbcOperations.update(sqlQuery, Map.of("userId", userId, "friendId", friendId));
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        final String sqlQuery = "delete from STATUS_FRIEND " +
                "where USER_ID = :userId and FRIEND_ID = :friendId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("friendId", friendId);
        jdbcOperations.update(sqlQuery, param);
    }

    @Override
    public List<User> getFriends(Long userId) {
        final String sqlQuery = "select USER_ID, LOGIN, BIRTHDAY, EMAIL, USER_NAME " +
                "from USERS where USER_ID in " +
                "(select FRIEND_ID " +
                "from STATUS_FRIEND where USER_ID = :userId)";
        return jdbcOperations.query(sqlQuery, Map.of("userId", userId), new UserRowMapper());
    }

    @Override
    public List<User> getListOfMutualFriends(Long userId, Long otherId) {
        final String sqlQuery = "select USER_ID, LOGIN, BIRTHDAY, EMAIL, USER_NAME " +
                "from USERS where USER_ID in " +
                "(select SF1.FRIEND_ID " +
                "from STATUS_FRIEND as SF1 " +
                "inner join STATUS_FRIEND as SF2 on SF1.FRIEND_ID = SF2.FRIEND_ID " +
                "where SF1.USER_ID = :userId and SF2.USER_ID = :otherId)";
        return jdbcOperations.query(sqlQuery,
                Map.of("userId", userId, "otherId", otherId),
                new UserRowMapper());
    }

    private MapSqlParameterSource getMapParameter(User user, boolean isCreate) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        if (!isCreate) map.addValue("userId", user.getId());
        return map;
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("USER_ID"),
                    rs.getString("EMAIL"),
                    rs.getString("LOGIN"),
                    rs.getString("USER_NAME"),
                    rs.getDate("BIRTHDAY").toLocalDate()
            );
        }
    }
}
