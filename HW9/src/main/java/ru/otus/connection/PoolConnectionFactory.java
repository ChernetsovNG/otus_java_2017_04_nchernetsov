package ru.otus.connection;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PoolConnectionFactory implements ConnectionFactory {
    private static final int POOL_SIZE = 3;
    private final ConnectionFactory factory;
    private final Queue<Connection> pool = new ConcurrentLinkedQueue<>();

    public PoolConnectionFactory(ConnectionFactory factory) {
        this.factory = factory;
        initPool();
    }

    @Override
    public Connection getConnection() {
        if (pool.isEmpty()) {
            java.sql.Connection connection = factory.getConnection();
            pool.add(new Connection(connection));
        }
        return pool.poll();
    }

    @Override
    public void dispose() {
        pool.forEach(connection -> {
            try {
                connection.setAutoCommit(false);
                connection.commit();      // завершаем незаконченные транзакции
                connection.superClose();  // закрываем соединение
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void initPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            java.sql.Connection connection = factory.getConnection();
            pool.add(new Connection(connection));
        }
    }

    private class Connection extends ConnectionDelegate {

        Connection(java.sql.Connection connection) {
            super(connection);
        }

        public void close() {
            pool.add(this);
        }

        void superClose() throws SQLException {
            super.close();
        }
    }
}
