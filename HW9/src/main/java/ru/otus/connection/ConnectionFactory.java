package ru.otus.connection;

import java.sql.Connection;

public interface ConnectionFactory {
    Connection getConnection();

    void dispose();
}
