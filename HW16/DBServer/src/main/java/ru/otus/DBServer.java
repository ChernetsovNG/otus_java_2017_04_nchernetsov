package ru.otus;

import ru.otus.app.Message;
import ru.otus.channel.SocketClientChannel;
import ru.otus.channel.SocketClientManagedChanel;
import ru.otus.dataSet.AddressDataSet;
import ru.otus.dataSet.PhoneDataSet;
import ru.otus.dataSet.UserDataSet;
import ru.otus.db.DBServiceImpl;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messages.GetUserIdByNameMessage;
import ru.otus.messages.HandshakeAnswerMessage;
import ru.otus.messages.HandshakeDemandMessage;
import ru.otus.messages.UserIdDBAnswerMessage;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.server.MessageServer.PORT2;

public class DBServer implements Addressee {
    private static final Logger logger = Logger.getLogger(DBServer.class.getName());

    private static final String HOST = "localhost";
    private static final int PAUSE_MS = 500;

    private static final int THREADS_NUMBER = 2;

    private volatile boolean handshakeSuccessful = false;
    private final Address address;

    private final DBServiceImpl dbService = new DBServiceImpl();

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);  // Порт для подключения к серверу принимаем в параметрах командной строки
        } else {
            port = PORT2;
        }
        // Запускаем сервер
        new DBServer(new Address("DBServer:" + port)).start(port);
    }

    public DBServer(Address address) {
        this.address = address;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start(int port) throws Exception {
        // Заполняем базу данных тестовыми данными
        for (int i = 1; i <= 10; i++) {
            UserDataSet userDataSet = new UserDataSet("User" + i,
                new AddressDataSet("Street" + i, i * i),
                Collections.singletonList(new PhoneDataSet(i, String.valueOf(i * i + 15))));

            dbService.save(userDataSet);
        }

        SocketClientChannel client = new SocketClientManagedChanel(HOST, port);
        client.init();

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        // Вначале отправляем на сервер сообщений Handshake сообщение
        Message handshakeMessage = new HandshakeDemandMessage(address, new Address("MessageServer"));
        client.send(handshakeMessage);

        // ожидаем ответа от сервера сообщений
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
                e.printStackTrace();
            }
        });

        // Принимаем сообщения от сервера сообщений
        executor.submit(() -> {
            try {
                while (!handshakeSuccessful) {
                    Thread.sleep(PAUSE_MS);
                }

                while (true) {
                    Message message = client.take();
                    logger.info("Message received: " + message.toString());
                    // Если приняли запрос на получение Id пользователя из БД, то отправляем ответ
                    if (message.getClassName().equals(GetUserIdByNameMessage.class.getName())) {
                        logger.info("Получен запрос на получение id пользователя");
                        long userId = dbService.getUserId(message.getPayload());
                        Message userIdDBAnswerMessage = new UserIdDBAnswerMessage(address, message.getFrom(), String.valueOf(userId));
                        logger.info("Клиенту отправлен ответ: " + userIdDBAnswerMessage);
                        client.send(userIdDBAnswerMessage);
                    }
                    Thread.sleep(PAUSE_MS);
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });

        // Поработаем некоторое время
        int time = 0;
        while (time < 50) {
            time += PAUSE_MS/1000;
            Thread.sleep(PAUSE_MS);
        }

        client.close();
        executor.shutdown();
    }

    @Override
    public Address getAddress() {
        return address;
    }

}
