package ru.otus;

import ru.otus.app.Message;
import ru.otus.channel.SocketClientChannel;
import ru.otus.channel.SocketClientManagedChanel;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messages.HandshakeAnswerMessage;
import ru.otus.messages.HandshakeDemandMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontendServer implements Addressee {
    private static final Logger logger = Logger.getLogger(FrontendServer.class.getName());

    private static final String HOST = "localhost";
    private static final int PAUSE_MS = 2000;
    private static final int MAX_MESSAGES_COUNT = 10;

    private final Address address;

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);  // Порт для подключения к серверу принимаем в параметрах командной строки
        new FrontendServer(new Address("FrontendServer:" + port)).start(port);
    }

    public FrontendServer(Address address) {
        this.address = address;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start(int port) throws Exception {
        logger.info("FrontendServer process started");

        SocketClientChannel client = new SocketClientManagedChanel(HOST, port);
        client.init();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        /*executorService.submit(() -> {
            try {
                while (true) {
                    Object msg = client.take();
                    System.out.println("Message received: " + msg.toString());
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });*/

        // Вначале отправляем на сервер сообщений Handshake сообщение
        Message handshakeMessage = new HandshakeDemandMessage(address, new Address("MessageServer"));
        client.send(handshakeMessage);

        // ожидаем ответа от сервера сообщений
        while (true) {
            Message handshakeAnswer = client.take();
            if (handshakeAnswer.getClassName().equals(HandshakeAnswerMessage.class.getName())) {
                logger.info("Получен ответ об установлении связи от MessageServer");
                break;
            } else {
                Thread.sleep(PAUSE_MS);
            }
        }

        // После этого начинаем основной цикл работы сервера
        /*int count = 0;
        while (count < MAX_MESSAGES_COUNT) {
            Message message = new PingMessage("FrontendServer " + port);
            client.send(message);
            System.out.println("Message sent: " + message.toString());
            Thread.sleep(PAUSE_MS);
            count++;
        }*/




        //client.close();
        //executorService.shutdown();
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
