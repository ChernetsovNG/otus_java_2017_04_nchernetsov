package ru.otus.orm;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.entity.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ORMTest {
    private static ORM orm = null;

    @BeforeClass
    public static void processEntityTest() throws Exception {
        orm = new ORM();
    }

    @AfterClass
    public static void afterAll() throws SQLException {
        orm.dispose();
    }

    @Before
    public void beforeTest() throws SQLException {
        orm.execQuery("TRUNCATE TABLE users;");
    }

    @Test
    public void insertSomeUsersInTableTest() {
        orm.save(new User(1, "Ivan", 25));
        orm.save(new User(5, "Maria", 19));

        try {
            int IvanID = orm.execQuery("SELECT id FROM users WHERE name = 'Ivan'", resultSet -> {
                resultSet.next();
                return resultSet.getInt(1);
            });

            int MariaID = orm.execQuery("SELECT id FROM users WHERE name = 'Maria'", resultSet -> {
                resultSet.next();
                return resultSet.getInt(1);
            });

            assertEquals(1, IvanID);
            assertEquals(5, MariaID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadUserTest() {
        orm.save(new User(2, "User2", 2));
        orm.save(new User(3, "User3", 3));

        User user2 = orm.load(2, User.class);
        User user3 = orm.load(3, User.class);

        assertEquals("User2", user2.getName());
        assertEquals("User3", user3.getName());
    }

    @Test
    public void loadAllUsersTest() {
        List<User> savedUsers = Arrays.asList(
            new User(1, "User1", 1),
            new User(2, "User2", 2),
            new User(3, "User3", 3));

        for (User user : savedUsers) {
            orm.save(user);
        }

        List<User> loadedUsers = orm.loadAll(User.class);

        assertTrue(savedUsers.containsAll(loadedUsers));
        assertTrue(loadedUsers.containsAll(savedUsers));
    }

}
