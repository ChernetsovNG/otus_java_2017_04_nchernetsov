package ru.otus.orm;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.main.User;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ORMTest {
    private static ORM orm = null;

    @BeforeClass
    public static void processEntityTest() throws Exception {
        orm = new ORM();
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

}