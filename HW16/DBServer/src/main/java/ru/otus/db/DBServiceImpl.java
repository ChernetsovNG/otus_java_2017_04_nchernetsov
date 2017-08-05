package ru.otus.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.cache.Element;
import ru.otus.dataSet.AddressDataSet;
import ru.otus.dataSet.PhoneDataSet;
import ru.otus.dataSet.UserDataSet;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;

public class DBServiceImpl implements DBService {
    private final SessionFactory sessionFactory;
    private final CacheEngine<Long, UserDataSet> cache;

    public DBServiceImpl() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:HW10_database");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        sessionFactory = createSessionFactory(configuration);

        // Создаём кеш
        cache = new CacheEngineImpl<>(5, 50_000, 0, false);
    }

    public String getLocalStatus() {
        return runInSession(session -> session.getTransaction().getStatus().name());
    }

    @Override
    public void init() {
    }

    @Override
    public long getUserId(String name) {
        return readByName(name).getId();
    }

    @Override
    public void save(UserDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
            cache.put(new Element<>(dataSet.getId(), dataSet));  // добавляем элемент в кеш
        }
    }

    @Override
    public UserDataSet read(long id) {
        // сперва ищем в кеше, если там нет, то обращаемся к базе
        Element<Long, UserDataSet> element = cache.get(id);

        UserDataSet userFromCache;
        if (element != null) {
            userFromCache = element.getValue();
        } else {
            userFromCache = null;
        }

        if (userFromCache != null) {
            return userFromCache;
        } else {
            return runInSession(session -> {
                UserDataSetDAO dao = new UserDataSetDAO(session);
                return dao.read(id);
            });
        }
    }

    @Override
    public UserDataSet readByName(String name) {
        // просматриваем все элементы в кеше в поисках нужного
        List<Element<Long, UserDataSet>> elementsFromCache = cache.getAll();
        for (Element<Long, UserDataSet> element : elementsFromCache) {
            UserDataSet userFromCache = element.getValue();
            if (userFromCache.getName().equals(name)) {
                return userFromCache;
            }
        }
        // если не нашли, то обращаемся к базе данных
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readByName(name);
        });
    }

    @Override
    public void deleteUserById(long id) {
        runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            // Удаляем запись из кеша
            cache.removeElement(id);
            // Удаляем из базы
            dao.deleteUserById(id);
            return null;
        });
    }

    @Override
    public void shutdown() {
        sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
