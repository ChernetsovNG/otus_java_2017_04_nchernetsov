package ru.otus.utils;

import org.junit.Test;

public class DataSourceTest {
    @Test
    public void printInfoTest() throws Exception {
        DataSource dataSource = new PostgresDataSource();
        dataSource.printInfo();
    }

}