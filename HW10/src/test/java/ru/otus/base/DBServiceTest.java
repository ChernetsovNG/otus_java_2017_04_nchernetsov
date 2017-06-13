package ru.otus.base;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.base.dataSets.AddressDataSet;
import ru.otus.base.dataSets.PhoneDataSet;
import ru.otus.base.dataSets.UserDataSet;
import ru.otus.dbService.DBServiceHibernateImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DBServiceTest {
    private static DBService dbService;

    @BeforeClass
    public static void createDBService() {
        dbService = new DBServiceHibernateImpl();
    }

    @AfterClass
    public static void shutdownDBService() {
        dbService.shutdown();
    }

    @Test
    public void getLocalStatusTest() throws Exception {
        String status = dbService.getLocalStatus();
        assertEquals("ACTIVE", status);
    }

    @Test
    public void saveAndReadTest() throws Exception {
        dbService.save(new UserDataSet("tully",
            new AddressDataSet("Lebedeva, 14", 164502),
            Arrays.asList(new PhoneDataSet(965, "12345")))
        );
        UserDataSet dataSet = dbService.readByName("tully");
        assertEquals("tully", dataSet.getName());
    }

    @Test
    public void readByNameTest() throws Exception {
        dbService.save(new UserDataSet("sully",
            new AddressDataSet("Lenina, 13", 526031),
            Arrays.asList(new PhoneDataSet(916, "23456")))
        );
        UserDataSet dataSet = dbService.readByName("sully");
        assertEquals("sully", dataSet.getName());
    }

    @Test
    public void readAllTest() throws Exception {
        dbService.save(new UserDataSet("User 1",
            new AddressDataSet("Lebedeva, 14", 164502),
            Arrays.asList(new PhoneDataSet(965, "12345")))
        );
        dbService.save(new UserDataSet("User 2",
            new AddressDataSet("Lenina, 13", 526031),
            Arrays.asList(
                new PhoneDataSet(916, "23456"),
                new PhoneDataSet(718, "911")
            ))
        );

        List<UserDataSet> dataSets = dbService.readAll();

        assertEquals(2, dataSets.size());
    }

}