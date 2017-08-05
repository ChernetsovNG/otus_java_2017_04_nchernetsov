package ru.otus;

import ru.otus.app.Message;
import ru.otus.channel.SocketClientChannel;
import ru.otus.channel.SocketClientManagedChanel;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messages.GetUserIdByNameMessage;
import ru.otus.messages.HandshakeAnswerMessage;
import ru.otus.messages.HandshakeDemandMessage;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.server.MessageServer.PORT1;
import static ru.otus.server.MessageServer.PORT2;

public class FrontendServer implements Addressee {
    private static final Logger logger = Logger.getLogger(FrontendServer.class.getName());

    private static final String HOST = "localhost";

    private static final int PAUSE_MS = 500;
    private static final int THREADS_NUMBER = 2;
    private static final int DB_QUERY_COUNT = 5;  // Количество обращений к базе данных

    private volatile boolean handshakeSuccessful = false;  // Удалось ли установить соединение с сервером

    private SocketClientChannel client;

    private final Address address;  // адрес данного сервера

    public FrontendServer(Address address) {
        this.address = address;
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);  // Порт для подключения к серверу принимаем в параметрах командной строки
        } else {
            port = PORT1;
        }
        new FrontendServer(new Address("FrontendServer:" + port)).start(port);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start(int port) throws Exception {
        logger.info("FrontendServer process started");

        client = new SocketClientManagedChanel(HOST, port);
        client.init();

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        // Вначале отправляем на сервер сообщений Handshake сообщение
        Message handshakeMessage = new HandshakeDemandMessage(address, new Address("MessageServer"));
        client.send(handshakeMessage);

        executor.submit(this::handshake);
        executor.submit(this::handleMessage);

        // После установления соединения отправляем сообщение к базе данных
        while (!handshakeSuccessful) {
            Thread.sleep(PAUSE_MS);
        }

        // Несколько раз сделаем запрос к базе данных
        for (int i = 0; i < DB_QUERY_COUNT; i++) {
            // запрашиваем у базы данных Id user'а
            Message messageToDB = generateRandomMessageToDB();

            client.send(messageToDB);

            System.out.println("Message sent: " + messageToDB);

            Thread.sleep(1500);
        }

        client.close();
        executor.shutdown();
    }

    private void handshake() {
        try {
            while (true) {
                Message handshakeAnswer = client.take();
                if (handshakeAnswer.getClassName().equals(HandshakeAnswerMessage.class.getName())) {
                    logger.info("Получен ответ об установлении связи от MessageServer");
                    handshakeSuccessful = true;
                    break;
                } else {
                    Thread.sleep(PAUSE_MS);
                }
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    private void handleMessage() {
        try {
            while (!handshakeSuccessful) {
                Thread.sleep(PAUSE_MS);
            }
            while (true) {
                Message answerMessage = client.take();  // Ждём ответа
                System.out.println("Receive answer with user id = " + answerMessage.getPayload());
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    // Генерируем случайный номер пользвоателя для запроса к случайной (из двух) базе
    private Message generateRandomMessageToDB() {
        Random random = new Random();

        boolean booleanDB = random.nextBoolean();
        int randomUserId = random.nextInt(10) + 1;

        Address dbServerAddress;
        if (booleanDB) {
            dbServerAddress = new Address("DBServer:" + PORT1);
        } else {
            dbServerAddress = new Address("DBServer:" + PORT2);
        }

        return new GetUserIdByNameMessage(address, dbServerAddress, "User" + randomUserId);
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
