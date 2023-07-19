package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(MpaDBStorage.class)
class MpaDBStorageTest {

    @Autowired
    private MpaStorage mpaStorage;

    @Test
    void getAllMpa() {
        List<String> names = new ArrayList<>();
        names.add("G");
        names.add("PG");
        names.add("PG-13");
        names.add("R");
        names.add("NC-17");
        List<Mpa> mpaList = mpaStorage.getAllMpa();
        for (int i = 0; i < names.size(); i++) {
            equalsMpa(i + 1, names.get(i), mpaList.get(i));
        }
    }

    @Test
    void getMpaById() {
        Mpa mpa = mpaStorage.getMpaById(3L);
        Mpa mpa1 = mpaStorage.getMpaById(5L);
        equalsMpa(3, "PG-13", mpa);
        equalsMpa(5, "NC-17", mpa1);
    }

    private void equalsMpa(int mpaId, String nameMpa, Mpa mpa) {
        assertEquals(mpaId, mpa.getId());
        assertEquals(nameMpa, mpa.getName());
    }
}