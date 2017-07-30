package ru.otus.nchernetsov;

import ru.otus.nchernetsov.runner.ProcessRunnerImpl;
import ru.otus.nchernetsov.server.MirrorServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageServerMain {
    private static final Logger logger = Logger.getLogger(MessageServerMain.class.getName());

    private static final String FRONTEND_START_COMMAND = "java -jar ../Frontend/target/frontend.jar";
    private static final int FRONTEND_START_DELAY_SEC = 5;

    private static final String DBSERVER_START_COMMAND = "java -jar ../DBServer/target/dbserver.jar";
    private static final int DBSERVER_START_DELAY_SEC = 5;

    public static void main(String[] args) throws Exception {
        new MessageServerMain().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // запускаем серверы для фронтэнда и для работы базы данных
        startDBServer(executorService);
        startFrontend(executorService);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Server");
        MirrorServer server = new MirrorServer();
        mbs.registerMBean(server, name);

        server.start();

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
