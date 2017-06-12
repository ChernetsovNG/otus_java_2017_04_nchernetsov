package ru.otus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    static Connection getConnection() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());

            String url = "jdbc:h2:mem:HW10_database";

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void printConnectionInfo() {
        try (Connection connection = getConnection()) {
            System.out.println("Connected to: " + connection.getMetaData().getURL());
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
