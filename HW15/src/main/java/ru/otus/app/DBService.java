package ru.otus.app;

import ru.otus.dataSets.UserDataSet;

import java.util.List;

public interface DBService {
    String getLocalStatus();

    void save(UserDataSet dataSet);

    UserDataSet read(long id);

    UserDataSet readByName(String name);

    long getUserId(String name);

    List<UserDataSet> readAll();

    List<UserDataSet> readAllFromCache();

    void deleteUserById(long id);

    void shutdown();

    int[] getCacheStats();
}
