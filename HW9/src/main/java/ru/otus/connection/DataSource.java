package ru.otus.connection;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection();

    Connection getConnection(String username, String password);

    void printInfo();
}
