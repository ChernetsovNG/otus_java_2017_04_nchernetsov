package ru.otus;

import ru.otus.dataSet.UserDataSet;

public interface DBService {
    void init();

    long getUserId(String name);

    void save(UserDataSet dataSet);

    UserDataSet read(long id);

    UserDataSet readByName(String name);

    void deleteUserById(long id);

    void shutdown();
}
