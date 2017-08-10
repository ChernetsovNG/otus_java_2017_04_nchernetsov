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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.server.MessageServer.PORT;

public class DBServer implements Addressee {
    private static final Logger LOG = Logger.getLogger(DBServer.class.getName());

    private static final int THREADS_NUMBER = 2;
    private static final String HOST = "localhost";

    private static final int PAUSE_MS = 500;

    private final CountDownLatch handshakeLatch = new CountDownLatch(1);

    private SocketClientChannel client;

    private final Address address;  // Адрес данного сервера

    private final DBServiceImpl dbService = new DBServiceImpl();

    public DBServer(Address address) {
        this.address = address;
    }

    public static void main(String[] args) throws Exception {
        int serverNum = Integer.parseInt(args[0]);
        new DBServer(new Address("DBServer:" + serverNum)).start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {
        // Заполняем базу данных тестовыми данными
        LOG.info("Init database and populate it");
        for (int i = 1; i <= 10; i++) {
            UserDataSet userDataSet = new UserDataSet("User" + i,
                new AddressDataSet("Street" + i, i * i),
                Collections.singletonList(new PhoneDataSet(i, String.valueOf(i * i + 15))));

            dbService.save(userDataSet);
        }

        client = new SocketClientManagedChanel(HOST, PORT);
        client.init();

        LOG.info("Connection with SocketServer is successful");

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        // Вначале отправляем на сервер сообщений Handshake сообщение
        Message handshakeMessage = new HandshakeDemandMessage(address, new Address("MessageServer"));
        client.send(handshakeMessage);

        LOG.info("Handshake message sending...");

        executor.submit(this::handshake);  // ожидаем ответа об успешном установлении соединения
        executor.submit(this::handleMessage);  // Принимаем сообщения от сервера сообщений

        // Поработаем некоторое время
        int time = 0;
        while (time < 50) {
            time += PAUSE_MS/1000;
            Thread.sleep(PAUSE_MS);
        }

        client.close();
        executor.shutdown();
    }

    private void handshake() {
        try {
            while (true) {
                Message handshakeAnswer = client.take();
                if (handshakeAnswer.getClassName().equals(HandshakeAnswerMessage.class.getName())) {
                    LOG.info("Получен ответ об установлении связи от MessageServer");
                    handshakeLatch.countDown();
                    break;
                } else {
                    Thread.sleep(PAUSE_MS);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage() {
        try {
            handshakeLatch.await();
            LOG.info("Начат цикл обработки сообщений");
            while (true) {
                Message message = client.take();
                LOG.info("Message received: " + message.toString());
                // Если приняли запрос на получение Id пользователя из БД, то отправляем ответ
                if (message.getClassName().equals(GetUserIdByNameMessage.class.getName())) {
                    LOG.info("Получен запрос на получение id пользователя");
                    long userId = dbService.getUserId(message.getPayload());
                    Message userIdDBAnswerMessage = new UserIdDBAnswerMessage(address, message.getFrom(), String.valueOf(userId));
                    LOG.info("Клиенту отправлен ответ: " + userIdDBAnswerMessage);
                    client.send(userIdDBAnswerMessage);
                }
                Thread.sleep(PAUSE_MS);
            }
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

}
