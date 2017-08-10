package ru.otus;

import ru.otus.app.Message;
import ru.otus.channel.SocketClientChannel;
import ru.otus.channel.SocketClientManagedChanel;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messages.AddressNotRegisteredMessage;
import ru.otus.messages.GetUserIdByNameMessage;
import ru.otus.messages.HandshakeAnswerMessage;
import ru.otus.messages.HandshakeDemandMessage;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.server.MessageServer.PORT;

public class FrontendServer implements Addressee {
    private static final Logger LOG = Logger.getLogger(FrontendServer.class.getName());

    public static final AtomicInteger frontendServerNum = new AtomicInteger(0);  // номер запущенного сервера
    private static final String HOST = "localhost";

    private static final int PAUSE_MS = 500;
    private static final int THREADS_NUMBER = 2;
    private static final int DB_QUERY_COUNT = 5;  // Количество обращений к базе данных

    private final CountDownLatch handshakeLatch = new CountDownLatch(1);  // ожидаем установления соединения с сервером,
                                                                          // после чего снимаем блокировку обработки сообщений
    private SocketClientChannel client;

    private final Address address;  // адрес данного сервера

    public FrontendServer(Address address) {
        this.address = address;
    }

    public static void main(String[] args) throws Exception {
        new FrontendServer(new Address("FrontendServer:" + frontendServerNum.incrementAndGet())).start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {
        LOG.info("FrontendServer process started");

        client = new SocketClientManagedChanel(HOST, PORT);
        client.init();

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        // Вначале отправляем на сервер сообщений Handshake сообщение
        Message handshakeMessage = new HandshakeDemandMessage(address, new Address("MessageServer"));
        client.send(handshakeMessage);

        executor.submit(this::handshake);
        executor.submit(this::handleMessage);

        // После установления соединения отправляем сообщение к базе данных
        handshakeLatch.await();

        // Несколько раз сделаем запрос к базе данных
        for (int i = 0; i < DB_QUERY_COUNT; i++) {
            // запрашиваем у базы данных Id user'а
            Message messageToDB = generateRandomMessageToDB();

            client.send(messageToDB);

            System.out.println("Message sent: " + messageToDB);

            Thread.sleep(2000);
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
                    handshakeLatch.countDown();  // снимаем блокировку
                    break;
                } else {
                    Thread.sleep(PAUSE_MS);
                }
            }
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, e.toString());
        }
    }

    private void handleMessage() {
        try {
            handshakeLatch.await();  // ждём, пока не установится соединение
            while (true) {
                Message answerMessage = client.take();  // Ждём ответа
                if (answerMessage.getClassName().equals(AddressNotRegisteredMessage.class.getName())) {
                    LOG.info("Receive answer that DBAddress not registered in MessageSystem");
                } else {
                    System.out.println("Receive answer with user id = " + answerMessage.getPayload());
                }
            }
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, e.toString());
        }
    }

    // Генерируем случайный номер пользвоателя для запроса к случайной (из двух) базе
    private Message generateRandomMessageToDB() {
        Random random = new Random();

        boolean booleanDB = random.nextBoolean();
        int randomUserId = random.nextInt(10) + 1;

        Address dbServerAddress;
        if (booleanDB) {
            dbServerAddress = new Address("DBServer:" + 1);
        } else {
            dbServerAddress = new Address("DBServer:" + 2);
        }

        return new GetUserIdByNameMessage(address, dbServerAddress, "User" + randomUserId);
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
