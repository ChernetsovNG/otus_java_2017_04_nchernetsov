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

    private static final int DB_QUERY_COUNT = 5;  // Сколько раз обращаемся к базе данных
    private static final int THREADS_NUMBER = 2;

    private volatile boolean handshakeSuccessful = false;

    private final Address address;

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);  // Порт для подключения к серверу принимаем в параметрах командной строки
        } else {
            port = PORT1;
        }
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

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        // Вначале отправляем на сервер сообщений Handshake сообщение
        Message handshakeMessage = new HandshakeDemandMessage(address, new Address("MessageServer"));
        client.send(handshakeMessage);

        executor.submit(() -> {
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
                logger.log(Level.SEVERE, e.getMessage());
            }
        });

        executor.submit(() -> {
            try {
                while (!handshakeSuccessful) {
                    Thread.sleep(PAUSE_MS);
                }

                while (true) {
                    Message answerMessage = client.take();  // Ждём ответа
                    System.out.println("Receive answer with user id = " + answerMessage.getPayload());
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });

        // После установления соединения отправляем сообщение к базе данных
        while (!handshakeSuccessful) {
            Thread.sleep(PAUSE_MS);
        }

        // Несколько раз сделаем запрос к базе данных
        for (int i = 0; i < DB_QUERY_COUNT; i++) {
            // Генерируем случайный номер пользвоателя для запроса к случайной (из двух) базе
            Random random = new Random();

            boolean booleanDB = random.nextBoolean();
            int randomUserId = random.nextInt(10) + 1;

            Address dbServerAddress;
            if (booleanDB) {
                dbServerAddress = new Address("DBServer:" + PORT1);
            } else {
                dbServerAddress = new Address("DBServer:" + PORT2);
            }

            Message getUserIdByNameMessage = new GetUserIdByNameMessage(address, dbServerAddress, "User" + randomUserId);  // запрашиваем у базы данных Id user'а

            client.send(getUserIdByNameMessage);
            System.out.println("Message sent: " + getUserIdByNameMessage.toString());

            Thread.sleep(1500);
        }

        client.close();
        executor.shutdown();
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
