package ru.otus.server;

import ru.otus.app.Message;
import ru.otus.app.MessageChannel;
import ru.otus.channel.SocketClientChannel;

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
    private static final int PORT1 = 5050;
    //private static final int PORT2 = 5051;
    private static final int MIRROR_DELAY = 100;

    private final ExecutorService executor;
    private final List<MessageChannel> channels;

    public MessageServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channels = new ArrayList<>();
    }

    public void start() throws Exception {
        executor.submit(this::mirror);

        try (ServerSocket serverSocket = new ServerSocket(PORT1)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept();  // blocks
                SocketClientChannel channel = new SocketClientChannel(client);
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
                    System.out.println("Mirroring the message: " + message.toString());
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
