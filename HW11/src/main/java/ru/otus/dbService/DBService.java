package ru.otus.dbService;

import ru.otus.base.dataSets.UserDataSet;

import java.util.List;

public interface DBService {
    String getLocalStatus();

    void save(UserDataSet dataSet);

    UserDataSet read(long id);

    UserDataSet readByName(String name);

    List<UserDataSet> readAll();

    void deleteUserById(long id);

    void shutdown();
}
