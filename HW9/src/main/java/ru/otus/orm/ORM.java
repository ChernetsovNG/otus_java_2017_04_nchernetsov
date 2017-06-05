package ru.otus.orm;

import org.reflections.Reflections;
import ru.otus.main.User;
import ru.otus.orm.handlers.TResultHandler;
import ru.otus.utils.ConnectionHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ORM implements Executor {
    private final Connection connection;
    private Map<Class<?>, String> tableNames = new HashMap<>();

    ORM() {
        connection = ConnectionHelper.getConnection();
        prepare();
    }

    private void prepare() {
        Reflections reflections = new Reflections("ru.otus");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Entity.class);

        for (Class<?> annotatedClass : annotatedClasses) {
            Table annotationTable = annotatedClass.getAnnotation(Table.class);
            tableNames.put(annotatedClass, annotationTable.name());
        }
    }

    @Override
    public void save(User user) {
        int id = user.getId();
        String name = user.getName();
        int age = user.getAge();

        // Имя таблицы в БД, соответствующей сущности User
        String tableName = tableNames.get(User.class);
        // Находим имена столбцов в таблице БД, соответствующие полям класса User
        String idColumnName = getIdColumnName();
        String nameColumnName = getNameColumnName();
        String ageColumnName = getAgeColumnName();

        String query = "INSERT INTO " + tableName + " (" +
            idColumnName + ", " +
            nameColumnName + ", " +
            ageColumnName + ") " +
            "VALUES (" +
            id + ", " +
            "'" + name + "'" +
            ", " + age +
            ");";

        try {
            execQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User load(long id, Class<?> clazz) {
        return null;
    }

    void execQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        stmt.close();
    }

    <T> T execQuery(String query, TResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();
        return value;
    }

    private String getIdColumnName() {
        String idColumnName = "";
        for (Field field : User.class.getDeclaredFields()) {
            if (field.getName().equals("id")) {
                if (field.isAnnotationPresent(Column.class)) {
                    idColumnName = field.getAnnotation(Column.class).name();
                }
            }
        }
        return idColumnName;
    }

    private String getNameColumnName() {
        String nameColumnName = "";
        for (Field field : User.class.getDeclaredFields()) {
            if (field.getName().equals("name")) {
                if (field.isAnnotationPresent(Column.class)) {
                    nameColumnName = field.getAnnotation(Column.class).name();
                }
            }
        }
        return nameColumnName;
    }

    private String getAgeColumnName() {
        String ageColumnName = "";
        for (Field field : User.class.getDeclaredFields()) {
            if (field.getName().equals("age")) {
                if (field.isAnnotationPresent(Column.class)) {
                    ageColumnName = field.getAnnotation(Column.class).name();
                }
            }
        }
        return ageColumnName;
    }

}
