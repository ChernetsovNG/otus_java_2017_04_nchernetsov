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
import ru.otus.messages.HandshakeAnswerMessage;
import ru.otus.messages.HandshakeDemandMessage;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBServer implements Addressee {
    private static final Logger logger = Logger.getLogger(DBServer.class.getName());

    private static final String HOST = "localhost";
    private static final int PAUSE_MS = 5000;
    private static final int MAX_MESSAGES_COUNT = 10;

    private final Address address;

    private final DBServiceImpl dbService = new DBServiceImpl();

    public static void main(String[] args) throws Exception {
        // Запускаем сервер
        int port = Integer.parseInt(args[0]);
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

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Вначале отправляем на сервер сообщений Handshake сообщение
        Message handshakeMessage = new HandshakeDemandMessage(address, new Address("DBServer"));
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

        /*executorService.submit(() -> {
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
            Message message = new PingMessage("DBServer " + port);
            client.send(message);
            System.out.println("Message sent: " + message.toString());
            Thread.sleep(PAUSE_MS);
            count++;
        }*/
            client.close();
            executorService.shutdown();
        }

    }

    @Override
    public Address getAddress() {
        return address;
    }

}
