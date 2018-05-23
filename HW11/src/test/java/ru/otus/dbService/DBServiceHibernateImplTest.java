package ru.otus.dbService;

import org.junit.*;
import ru.otus.base.dataSets.AddressDataSet;
import ru.otus.base.dataSets.PhoneDataSet;
import ru.otus.base.dataSets.UserDataSet;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class DBServiceHibernateImplTest {
    private static DBService dbService;

    @Before
    public void createDBService() {
        dbService = new DBServiceHibernateImpl();
    }

    @After
    public void shutdownDBService() {
        dbService.shutdown();
    }

    @Test
    public void saveAndReadTest() {
        UserDataSet userDataSet1 = new UserDataSet("User 1",
            new AddressDataSet("Street 1", 1),
            Collections.singletonList(new PhoneDataSet(1, "1")));

        UserDataSet userDataSet2 = new UserDataSet("User 2",
            new AddressDataSet("Street 2", 2),
            Collections.singletonList(new PhoneDataSet(2, "2")));

        UserDataSet userDataSet3 = new UserDataSet("User 3",
            new AddressDataSet("Street 3", 3),
            Collections.singletonList(new PhoneDataSet(3, "3")));

        dbService.save(userDataSet1);
        dbService.save(userDataSet2);
        dbService.save(userDataSet3);

        dbService.read(1);
        dbService.read(2);
        dbService.read(3);

        assertEquals(3, dbService.getCacheStats()[0]);
        assertEquals(0, dbService.getCacheStats()[1]);
        assertEquals(3, dbService.getCacheStats()[2]);
    }

    @Test
    public void removeElementFromCacheByLifeTimeTest() throws Exception {
        UserDataSet userDataSet1 = new UserDataSet("User 1",
            new AddressDataSet("Street 1", 1),
            Collections.singletonList(new PhoneDataSet(1, "1")));

        dbService.save(userDataSet1);

        dbService.read(1);

        assertEquals(1, dbService.getCacheStats()[0]);

        TimeUnit.MILLISECONDS.sleep(3200);  // элементы в кеше "живут" 3 секунды

        dbService.read(1);

        assertEquals(1, dbService.getCacheStats()[0]);
        assertEquals(1, dbService.getCacheStats()[1]);
        assertEquals(0, dbService.getCacheStats()[2]);  //кол-во элементов = 0
    }

    @Test
    public void readAllFromCacheTest() {
        UserDataSet userDataSet1 = new UserDataSet("User 1",
            new AddressDataSet("Street 1", 1),
            Collections.singletonList(new PhoneDataSet(1, "1")));

        UserDataSet userDataSet2 = new UserDataSet("User 2",
            new AddressDataSet("Street 2", 2),
            Collections.singletonList(new PhoneDataSet(2, "2")));

        UserDataSet userDataSet3 = new UserDataSet("User 3",
            new AddressDataSet("Street 3", 3),
            Collections.singletonList(new PhoneDataSet(3, "3")));

        dbService.save(userDataSet1);
        dbService.save(userDataSet2);
        dbService.save(userDataSet3);

        List<UserDataSet> users = dbService.readAllFromCache();

        assertEquals(3, users.size());

        assertEquals(3, dbService.getCacheStats()[0]);
        assertEquals(0, dbService.getCacheStats()[1]);
        assertEquals(3, dbService.getCacheStats()[2]);
    }

    @Test
    public void removeElementFromCacheTest() {
        UserDataSet userDataSet1 = new UserDataSet("User 1",
            new AddressDataSet("Street 1", 1),
            Collections.singletonList(new PhoneDataSet(1, "1")));

        UserDataSet userDataSet2 = new UserDataSet("User 2",
            new AddressDataSet("Street 2", 2),
            Collections.singletonList(new PhoneDataSet(2, "2")));

        UserDataSet userDataSet3 = new UserDataSet("User 3",
            new AddressDataSet("Street 3", 3),
            Collections.singletonList(new PhoneDataSet(3, "3")));

        dbService.save(userDataSet1);
        dbService.save(userDataSet2);
        dbService.save(userDataSet3);

        assertEquals(3, dbService.getCacheStats()[2]);

        dbService.deleteUserById(1);  // здесь удаляем элемент также из кеша

        assertEquals(2, dbService.getCacheStats()[2]);
    }

    @Test
    public void overflowCacheTest() {
        for (int i = 1; i <= 6; i++) {
            UserDataSet userDataSet = new UserDataSet("User " + i,
                new AddressDataSet("Street " + i, i),
                Collections.singletonList(new PhoneDataSet(i, String.valueOf(i))));

            dbService.save(userDataSet);
        }
        assertEquals(5, dbService.getCacheStats()[2]);
    }

}