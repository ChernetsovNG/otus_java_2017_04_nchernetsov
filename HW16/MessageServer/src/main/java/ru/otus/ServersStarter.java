package ru.otus;

import ru.otus.runner.ProcessRunnerImpl;
import ru.otus.server.MessageServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServersStarter {
    private static final Logger logger = Logger.getLogger(ServersStarter.class.getName());

    private static String FRONTEND_START_COMMAND = "java -jar ../FrontendServer/target/frontend.jar";
    private static final int FRONTEND_START_DELAY_SEC = 1;

    private static final String DBSERVER_START_COMMAND = "java -jar ../DBServer/target/dbserver.jar";
    private static final int DBSERVER_START_DELAY_SEC = 1;

    public static void main(String[] args) throws Exception {
        new ServersStarter().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        // запускаем серверы для фронтэнда и для работы базы данных
        //startDBServer(executorService);
        startFrontend(executorService);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus.nchernetsov:type=Server");
        MessageServer messageServer = new MessageServer();
        mbs.registerMBean(messageServer, name);

        messageServer.start();

        executorService.shutdown();
    }

    private void startDBServer(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(DBSERVER_START_COMMAND);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, DBSERVER_START_DELAY_SEC, TimeUnit.SECONDS);
    }

    private void startFrontend(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(FRONTEND_START_COMMAND);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, FRONTEND_START_DELAY_SEC, TimeUnit.SECONDS);
    }

}
