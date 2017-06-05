package ru.otus.utils;

import org.junit.Test;

import static ru.otus.utils.ConnectionHelper.printDBInfo;

public class ConnectionHelperTest {
    @Test
    public void printDBInfoTest() throws Exception {
        printDBInfo();
    }

}