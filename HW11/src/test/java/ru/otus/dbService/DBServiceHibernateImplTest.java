package ru.otus.dbService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.base.dataSets.AddressDataSet;
import ru.otus.base.dataSets.PhoneDataSet;
import ru.otus.base.dataSets.UserDataSet;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class DBServiceHibernateImplTest {
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
    public void saveAndReadTest() throws Exception {
        UserDataSet userDataSet1 = new UserDataSet("User 1",
            new AddressDataSet("Street 1", 1),
            Arrays.asList(new PhoneDataSet(1, "1")));

        UserDataSet userDataSet2 = new UserDataSet("User 2",
            new AddressDataSet("Street 2", 2),
            Arrays.asList(new PhoneDataSet(2, "2")));

        UserDataSet userDataSet3 = new UserDataSet("User 3",
            new AddressDataSet("Street 3", 3),
            Arrays.asList(new PhoneDataSet(3, "3")));

        dbService.save(userDataSet1);
        dbService.save(userDataSet2);
        dbService.save(userDataSet3);

        dbService.read(1);
        dbService.read(2);
        dbService.read(3);

        assertEquals(3, dbService.getCacheStats()[0]);
        assertEquals(0, dbService.getCacheStats()[1]);
    }

    @Test
    public void removeElementFromCacheByTimeTest() throws Exception {
        UserDataSet userDataSet1 = new UserDataSet("User 1",
            new AddressDataSet("Street 1", 1),
            Arrays.asList(new PhoneDataSet(1, "1")));

        dbService.save(userDataSet1);

        dbService.read(1);

        assertEquals(1, dbService.getCacheStats()[0]);

        TimeUnit.MILLISECONDS.sleep(3200);

        dbService.read(1);

        assertEquals(1, dbService.getCacheStats()[0]);
        assertEquals(1, dbService.getCacheStats()[1]);
    }

    @Test
    public void readAllFromCacheTest() throws Exception {
        UserDataSet userDataSet1 = new UserDataSet("User 1",
            new AddressDataSet("Street 1", 1),
            Arrays.asList(new PhoneDataSet(1, "1")));

        UserDataSet userDataSet2 = new UserDataSet("User 2",
            new AddressDataSet("Street 2", 2),
            Arrays.asList(new PhoneDataSet(2, "2")));

        UserDataSet userDataSet3 = new UserDataSet("User 3",
            new AddressDataSet("Street 3", 3),
            Arrays.asList(new PhoneDataSet(3, "3")));

        dbService.save(userDataSet1);
        dbService.save(userDataSet2);
        dbService.save(userDataSet3);

        List<UserDataSet> users = dbService.readAllFromCache();

        assertEquals(3, users.size());

        assertEquals(3, dbService.getCacheStats()[0]);
        assertEquals(0, dbService.getCacheStats()[1]);
    }
}