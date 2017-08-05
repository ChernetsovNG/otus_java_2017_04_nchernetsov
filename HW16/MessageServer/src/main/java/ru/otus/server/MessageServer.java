package ru.otus.server;

import ru.otus.app.Message;
import ru.otus.app.MessageChannel;
import ru.otus.channel.SocketClientChannel;
import ru.otus.messages.PingMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MessageServer implements MessageServerMBean {
    private static final Logger logger = Logger.getLogger(MessageServer.class.getName());

    private static final int THREADS_NUMBER = 2;
    public static final int PORT1 = 5050;
    public static final int PORT2 = 5051;
    private static final int MIRROR_DELAY = 100;

    private final ExecutorService executor;
    private final List<MessageChannel> channels;

    public MessageServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channels = new ArrayList<>();
    }

    public void start() throws Exception {
        executor.submit(this::mirror);

        // Ждём подключения к серверу на двух портах
        try (ServerSocket serverSocket1 = new ServerSocket(PORT1);
             ServerSocket serverSocket2 = new ServerSocket(PORT2)) {

            logger.info("Server started on port: " + serverSocket1.getLocalPort());
            logger.info("Server started on port: " + serverSocket2.getLocalPort());

            while (!executor.isShutdown()) {
                Socket client = serverSocket1.accept();  // blocks
                SocketClientChannel channel = new SocketClientChannel(client);
                channel.init();
                channels.add(channel);

                client = serverSocket2.accept();  // blocks
                channel = new SocketClientChannel(client);
                channel.init();
                channels.add(channel);
            }
        }
    }

    private Object mirror() throws InterruptedException {
        while (true) {
            for (MessageChannel channel : channels) {
                Message message = channel.poll();
                if (message != null) {
                    System.out.println("Mirroring the message: " + message.toString() + " from: " + ((PingMessage) message).getFrom());
                    channel.send(message);
                }
            }
            Thread.sleep(MIRROR_DELAY);
        }
    }

    @Override
    public boolean getRunning() {
        return true;
    }

    @Override
    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            logger.info("Bye.");
        }
    }
}
