package ru.otus;

import ru.otus.app.Msg;
import ru.otus.channel.SocketClientChannel;
import ru.otus.messages.PingMsg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBServer {
    private static final Logger logger = Logger.getLogger(DBServer.class.getName());

    private static final String HOST = "localhost";
    private static final int PORT = 5052;
    private static final int PAUSE_MS = 5000;
    private static final int MAX_MESSAGES_COUNT = 10;


    public static void main(String[] args) throws Exception {
        new DBServer().start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {
        SocketClientChannel client = new SocketClientManagedChanel(HOST, PORT);
        client.init();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Object msg = client.take();
                    System.out.println("Message received: " + msg.toString());
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });

        int count = 0;
        while (count < MAX_MESSAGES_COUNT) {
            Msg msg = new PingMsg();
            client.send(msg);
            System.out.println("Message sent: " + msg.toString());
            Thread.sleep(PAUSE_MS);
            count++;
        }
        client.close();
        executorService.shutdown();
    }

}
