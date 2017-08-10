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

public class ServersStarter {
    private static final Logger LOG = Logger.getLogger(ServersStarter.class.getName());

    private static String FRONTEND_START_COMMAND = "java -jar /home/n_chernetsov/Education/Java_Otus/otus_java_2017_04_nchernetsov/HW16/FrontendServer/target/frontend.jar ";
    //private static String FRONTEND_START_COMMAND = "java -jar ../HW16/FrontendServer/target/frontend.jar ";
    private static final int FRONTEND_START_DELAY_SEC = 3;

    private static final String DBSERVER_START_COMMAND = "java -jar /home/n_chernetsov/Education/Java_Otus/otus_java_2017_04_nchernetsov/HW16/DBServer/target/dbserver.jar ";
    //private static final String DBSERVER_START_COMMAND = "java -jar ../HW16/DBServer/target/dbserver.jar ";
    private static final int DBSERVER_START_DELAY_SEC = 2;

    public static void main(String[] args) throws Exception {
        new ServersStarter().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

        // запускаем по два сервера для фронтэнда и для работы базы данных
        startDBServer(executorService, 1);
        startDBServer(executorService, 2);
        startFrontend(executorService, 1);
        startFrontend(executorService, 2);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus.nchernetsov:type=Server");
        MessageServer messageServer = new MessageServer();
        mbs.registerMBean(messageServer, name);

        messageServer.start();

        executorService.shutdown();
    }

    private void startFrontend(ScheduledExecutorService executorService, int serverNum) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(FRONTEND_START_COMMAND + serverNum);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage());
            }
        }, FRONTEND_START_DELAY_SEC, TimeUnit.SECONDS);
    }

    private void startDBServer(ScheduledExecutorService executorService, int serverNum) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(DBSERVER_START_COMMAND + serverNum);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage());
            }
        }, DBSERVER_START_DELAY_SEC, TimeUnit.SECONDS);
    }

}
