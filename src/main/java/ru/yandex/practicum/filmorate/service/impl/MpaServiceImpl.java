package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaRepository;

    @Autowired
    public MpaServiceImpl(MpaStorage mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaRepository.getAllMpa();
    }

    @Override
    public Mpa getMpaById(Long mpaId) {
        Mpa mpa = mpaRepository.getMpaById(mpaId);
        if (mpa == null) {
            log.warn("Error");
            log.warn("mpaId: " + mpaId + " рейтинг не найден.");
            throw new NotFoundException("Рейтинг не найден.");
        }
        return mpa;
    }
}
