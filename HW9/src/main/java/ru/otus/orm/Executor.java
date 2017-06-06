package ru.otus.orm;

import ru.otus.main.User;

interface Executor {
    void save(User user);
    User load(long id, Class<?> clazz);
}
