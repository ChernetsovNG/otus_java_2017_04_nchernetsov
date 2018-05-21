package ru.otus.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionFactory implements ConnectionFactory {
    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
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
    public void dispose() {
        // do nothing
    }
}
