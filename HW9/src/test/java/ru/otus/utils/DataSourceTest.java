package ru.otus.utils;

import org.junit.Test;
import ru.otus.connection.DataSource;
import ru.otus.connection.PostgresDataSource;

public class DataSourceTest {
    @Test
    public void printInfoTest() throws Exception {
        DataSource dataSource = new PostgresDataSource();
        dataSource.printInfo();
    }

}