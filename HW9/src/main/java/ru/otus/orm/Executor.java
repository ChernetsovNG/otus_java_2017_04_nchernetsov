package ru.otus.orm;

import ru.otus.main.User;

public interface Executor {
    void save(User user);
    User load(long id, Class<?> clazz);
}
