package ru.otus;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class ConnectionHelperTest {
    @Test
    public void printConnectionInfoTest() throws Exception {
        ConnectionHelper.printConnectionInfo();
    }

}