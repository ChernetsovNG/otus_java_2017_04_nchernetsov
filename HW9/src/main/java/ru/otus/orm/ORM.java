package ru.otus.orm;

import org.reflections.Reflections;
import ru.otus.entity.User;
import ru.otus.orm.handlers.TResultHandler;
import ru.otus.utils.PostgresDataSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static ru.otus.utils.ReflectionHelper.instantiate;
import static ru.otus.utils.ReflectionHelper.setFieldValue;

public class ORM implements Executor {
    private final Connection connection;
    // Карта вида (Класс - Имя таблицы в БД)
    private final Map<Class<?>, String> tableNames = new HashMap<>();
    // Карта вида (Класс - Карта (поле класса - столбец в таблице))
    private final Map<Class<?>, DataSetDescriptor> matchClassFieldsAndTablesColumnMap = new HashMap<>();

    ORM() {
        connection = new PostgresDataSource().getConnection();
        prepareObjectRelationalMapping();
    }

    private void prepareObjectRelationalMapping() {
        Reflections reflections = new Reflections("ru.otus");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Entity.class);

        // Обходим классы с аннотацией @Entity
        for (Class<?> annotatedClass : annotatedClasses) {
            // Имя таблицы, соответствующей классу
            Table annotationTable = annotatedClass.getAnnotation(Table.class);
            tableNames.put(annotatedClass, annotationTable.name());

            // Поля класса и соответствующие столбцы таблицы
            DataSetDescriptor classFieldColumnNameMap = new DataSetDescriptor();

            for (Field field : annotatedClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    String fieldName = field.getName();
                    String columnName = field.getAnnotation(Column.class).name();

                    classFieldColumnNameMap.put(fieldName, columnName);
                }
            }

            matchClassFieldsAndTablesColumnMap.put(annotatedClass, classFieldColumnNameMap);
        }
    }

    @Override
    public void save(User user) {
        // Имя таблицы в БД, соответствующей сущности User
        String tableName = tableNames.get(User.class);
        // Находим имена столбцов в таблице БД, соответствующие полям класса User
        DataSetDescriptor userDescriptor = matchClassFieldsAndTablesColumnMap.get(User.class);

        String query = insertUserQuery(tableName, userDescriptor, user);

        try {
            execQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User load(long id, Class<?> clazz) {
        String tableName = tableNames.get(clazz);

        DataSetDescriptor classFieldColumnNameMap = matchClassFieldsAndTablesColumnMap.get(clazz);

        Object[] columns = classFieldColumnNameMap.values().toArray();  // столбцы таблицы

        String query = selectUserQuery(tableName, columns, id);

        try {
            Map<String, Object> queryResultMap = execQuery(query, resultSet -> {
                Map<String, Object> map = new HashMap<>();
                resultSet.next();
                for (Object column : columns) {
                    String columnStr = (String) column;
                    map.put(columnStr, resultSet.getObject(columnStr));
                }
                return map;
            });
            return createUserFromQueryResult(clazz, classFieldColumnNameMap, queryResultMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> loadAll(Class<?> clazz) {
        List<User> result = new ArrayList<>();

        String tableName = tableNames.get(clazz);

        DataSetDescriptor classFieldColumnNameMap = matchClassFieldsAndTablesColumnMap.get(clazz);

        Object[] columns = classFieldColumnNameMap.values().toArray();  // столбцы таблицы

        String query = selectAllUsersQuery(tableName, columns);

        try {
            List<Map<String, Object>> queryResultListOfMaps = execQuery(query, resultSet -> {
                List<Map<String, Object>> listOfMaps = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (Object column : columns) {
                        String columnStr = (String) column;
                        map.put(columnStr, resultSet.getObject(columnStr));
                    }
                    listOfMaps.add(map);
                }
                return listOfMaps;
            });
            for (Map<String, Object> queryResultMap : queryResultListOfMaps) {
                User user = createUserFromQueryResult(clazz, classFieldColumnNameMap, queryResultMap);
                result.add(user);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    // Создаём новый объект нужного класса и записываем в соответствующие
    // поля результаты из запроса к БД
    private User createUserFromQueryResult(Class<?> clazz, DataSetDescriptor dataSetDescriptor,
                                           Map<String, Object> queryResultMap) {
        Object newObject = instantiate(clazz);
        for (Map.Entry<String, String> entry : dataSetDescriptor.entrySet()) {
            String fieldName = entry.getKey();
            String columnName = entry.getValue();
            setFieldValue(newObject, fieldName, queryResultMap.get(columnName));
        }
        return (User) newObject;
    }

    private String insertUserQuery(String tableName, DataSetDescriptor userDescriptor, User user) {
        String idColumnName = userDescriptor.get("id");
        String nameColumnName = userDescriptor.get("name");
        String ageColumnName = userDescriptor.get("age");

        long id = user.getId();
        String name = user.getName();
        int age = user.getAge();

        return "INSERT INTO " + tableName + " (" +
            idColumnName + ", " +
            nameColumnName + ", " +
            ageColumnName + ") " +
            "VALUES (" +
            id + ", " +
            "'" + name + "'" +
            ", " + age +
            ");";
    }

    private String selectUserQuery(String tableName, Object[] columns, long id) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        for (int i = 0; i < columns.length - 1; i++) {
            sb.append((String) columns[i]).append(", ");
        }
        sb.append((String) columns[columns.length - 1]).append(" ");
        sb.append("FROM ").append(tableName).append(" ")
            .append("WHERE id = ").append(id).append(";");

        return sb.toString();
    }

    private String selectAllUsersQuery(String tableName, Object[] columns) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        for (int i = 0; i < columns.length - 1; i++) {
            sb.append((String) columns[i]).append(", ");
        }
        sb.append((String) columns[columns.length - 1]).append(" ");
        sb.append("FROM ").append(tableName).append(";");

        return sb.toString();
    }

}
