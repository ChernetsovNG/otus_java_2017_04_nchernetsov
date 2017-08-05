package ru.otus;

import ru.otus.runner.ProcessRunnerImpl;
import ru.otus.server.MessageServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.server.MessageServer.PORT1;
import static ru.otus.server.MessageServer.PORT2;

public class ServersStarter {
    private static final Logger logger = Logger.getLogger(ServersStarter.class.getName());

    // Будем запускать по два сервера на разных портах
    private static String FRONTEND_START_COMMAND_PORT1 = "java -jar ../FrontendServer/target/frontend.jar " + PORT1;
    private static String FRONTEND_START_COMMAND_PORT2 = "java -jar ../FrontendServer/target/frontend.jar " + PORT2;
    private static final int FRONTEND_START_DELAY_SEC = 2;

    private static final String DBSERVER_START_COMMAND_PORT1 = "java -jar ../DBServer/target/dbserver.jar " + PORT1;
    private static final String DBSERVER_START_COMMAND_PORT2 = "java -jar ../DBServer/target/dbserver.jar " + PORT2;
    private static final int DBSERVER_START_DELAY_SEC = 2;

    public static void main(String[] args) throws Exception {
        new ServersStarter().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        // запускаем по два сервера для фронтэнда и для работы базы данных (на разных портах)
        startDBServer(executorService, PORT1);
        startDBServer(executorService, PORT2);
        startFrontend(executorService, PORT1);
        startFrontend(executorService, PORT2);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus.nchernetsov:type=Server");
        MessageServer messageServer = new MessageServer();
        mbs.registerMBean(messageServer, name);

        messageServer.start();

        executorService.shutdown();
    }

    private void startDBServer(ScheduledExecutorService executorService, int port) {
        executorService.schedule(() -> {
            try {
                switch (port) {
                    case PORT1:
                        new ProcessRunnerImpl().start(DBSERVER_START_COMMAND_PORT1);
                        break;
                    case PORT2:
                        new ProcessRunnerImpl().start(DBSERVER_START_COMMAND_PORT2);
                        break;
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, DBSERVER_START_DELAY_SEC, TimeUnit.SECONDS);
    }

    private void startFrontend(ScheduledExecutorService executorService, int port) {
        executorService.schedule(() -> {
            try {
                switch (port) {
                    case PORT1:
                        new ProcessRunnerImpl().start(FRONTEND_START_COMMAND_PORT1);
                        break;
                    case PORT2:
                        new ProcessRunnerImpl().start(FRONTEND_START_COMMAND_PORT2);
                        break;
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, FRONTEND_START_DELAY_SEC, TimeUnit.SECONDS);
    }

}
