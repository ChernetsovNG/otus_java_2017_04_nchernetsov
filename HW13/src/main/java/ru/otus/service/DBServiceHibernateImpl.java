package ru.otus.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.base.dataSets.AddressDataSet;
import ru.otus.base.dataSets.PhoneDataSet;
import ru.otus.base.dataSets.UserDataSet;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.Element;
import ru.otus.service.dao.UserDataSetDAO;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DBServiceHibernateImpl implements DBService {
    private final SessionFactory sessionFactory;
    private final CacheEngine<Long, UserDataSet> cacheEngine;

    public DBServiceHibernateImpl(CacheEngine cacheEngine) {
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

        this.cacheEngine = cacheEngine;
    }

    public String getLocalStatus() {
        return runInSession(session -> session.getTransaction().getStatus().name());
    }

    public void save(UserDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
            cacheEngine.put(new Element<>(dataSet.getId(), dataSet));  // добавляем элемент в кеш
        }
    }

    public UserDataSet read(long id) {
        // сперва ищем в кеше, если там нет, то обращаемся к базе
        Element<Long, UserDataSet> element = cacheEngine.get(id);

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

    public UserDataSet readByName(String name) {
        // просматриваем все элементы в кеше в поисках нужного
        List<Element<Long, UserDataSet>> elementsFromCache = cacheEngine.getAll();
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

    public List<UserDataSet> readAllFromCache() {
        List<Element<Long, UserDataSet>> elementsFromCache = cacheEngine.getAll();
        return elementsFromCache.stream().map(Element::getValue).collect(Collectors.toList());
    }

    public List<UserDataSet> readAll() {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readAll();
        });
    }

    public void deleteUserById(long id) {
        runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            // Удаляем запись из кеша
            cacheEngine.removeElement(id);
            // Удаляем из базы
            dao.deleteUserById(id);
            return null;
        });
    }

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

    @Override
    public int[] getCacheStats() {
        int[] res = new int[3];

        res[0] = cacheEngine.getHitCount();
        res[1] = cacheEngine.getMissCount();
        res[2] = cacheEngine.getElementsCount();

        return res;
    }
}
