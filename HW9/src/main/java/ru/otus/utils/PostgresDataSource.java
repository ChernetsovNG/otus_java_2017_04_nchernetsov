package ru.otus.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDataSource implements DataSource {

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            String url = "jdbc:postgresql://" +  // db type
                "localhost:" +               // host name
                "5432/" +                    // port
                "otus_test_db?" +            // db name
                "user=postgres&" +           // login
                "password=postgres";         // password

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) {
        try {
            String url = "jdbc:postgresql://" +
                "localhost:" +
                "5432/" +
                "otus_test_db?" +
                "user=" + username + "&" +
                "password=" + password;

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printInfo() {
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
