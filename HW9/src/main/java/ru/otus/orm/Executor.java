package ru.otus.orm;

import ru.otus.entity.User;

import java.util.List;

interface Executor {
    void save(User user);

    User load(long id, Class<?> clazz);

    List<User> loadAll(Class<?> clazz);
}
