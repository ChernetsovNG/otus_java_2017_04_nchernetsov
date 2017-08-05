package ru.otus;

import ru.otus.app.Message;
import ru.otus.channel.SocketClientChannel;
import ru.otus.channel.SocketClientManagedChanel;
import ru.otus.messages.PingMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontendServer {
    private static final Logger logger = Logger.getLogger(FrontendServer.class.getName());

    private static final String HOST = "localhost";
    private static final int PAUSE_MS = 5000;
    private static final int MAX_MESSAGES_COUNT = 10;


    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);  // Порт для подключения к серверу принимаем в параметрах командной строки
        new FrontendServer().start(port);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start(int port) throws Exception {
        logger.info("FrontendServer process started");

        SocketClientChannel client = new SocketClientManagedChanel(HOST, port);
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
            Message message = new PingMessage("FrontendServer " + port);
            client.send(message);
            System.out.println("Message sent: " + message.toString());
            Thread.sleep(PAUSE_MS);
            count++;
        }
        client.close();
        executorService.shutdown();
    }

}
