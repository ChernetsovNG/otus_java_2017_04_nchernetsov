package ru.otus.server;

import ru.otus.app.Message;
import ru.otus.app.MessageChannel;
import ru.otus.channel.SocketClientChannel;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messages.HandshakeAnswerMessage;
import ru.otus.messages.HandshakeDemandMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageServer implements MessageServerMBean, Addressee {
    private static final Logger logger = Logger.getLogger(MessageServer.class.getName());

    private static final int THREADS_NUMBER = 4;
    private static final int CLIENTS_NUMBER = 4;
    public static final int PORT1 = 5050;
    public static final int PORT2 = 5051;
    private static final int MESSAGE_DELAY = 100;

    private final ExecutorService executor;
    private final Map<MessageChannel, Address> connectionMap;  // карта вида <Канал для передачи сообщения - Соответствующий ему адрес>
    private static volatile boolean isConnectionMapInitialized = false;
    private final Address address;

    public MessageServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        connectionMap = new HashMap<>();
        address = new Address("MessageServer");
    }

    public static void main(String[] args) {
        try {
            new MessageServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        executor.submit(this::handshake);
        executor.submit(this::messageHandle);

        // Ждём подключения к серверу на двух портах. Для подключенных серверов создаём каналы для связи
        // и сохраняем эти каналы в карте
        try (ServerSocket serverSocket1 = new ServerSocket(PORT1);
             ServerSocket serverSocket2 = new ServerSocket(PORT2)) {

            logger.info("Server started on port: " + serverSocket1.getLocalPort());
            logger.info("Server started on port: " + serverSocket2.getLocalPort());

            while (!executor.isShutdown()) {
                Socket client1 = serverSocket1.accept();  // blocks

                logger.info("Client connect: " + client1);
                SocketClientChannel channel1 = new SocketClientChannel(client1);
                channel1.init();
                connectionMap.put(channel1, null);

                Socket client2 = serverSocket2.accept();  // blocks

                logger.info("Client connect: " + client2);
                SocketClientChannel channel2 = new SocketClientChannel(client2);
                channel2.init();
                connectionMap.put(channel2, null);
            }
        }
    }

    // Основная процедура обработки сообщений. Получает сообщение и пересылает его по нужному адресу
    private void messageHandle() {
        // Ждём, пока не будет инициализирована карта адресов
        while (!isConnectionMapInitialized) {
            try {
                Thread.sleep(MESSAGE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Запускаем процедуру обработки сообщений
        while (true) {
            for (Map.Entry<MessageChannel, Address> entry : connectionMap.entrySet()) {
                MessageChannel channel = entry.getKey();

                Message message = channel.poll();
                if (message != null) {
                    logger.info("MessageServer receive the message from: " + message.getFrom() + ". Receive it to: " + message.getTo() + ". " + message);
                    getChannelByAddress(message.getTo()).send(message);
                }
            }
            try {
                Thread.sleep(MESSAGE_DELAY);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }

    // Принимаем идентифицирующие сообщение, и сохраняем в карте соответствие адресата и его канала
    private void handshake() {
        // Ждём, пока подключатся все клиенты
        while (connectionMap.size() < CLIENTS_NUMBER) {
            try {
                Thread.sleep(MESSAGE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // до тех пор, пока все адреса связанных каналов не будут успешно определены, продолжаем этот цикл
        while (true) {
            // После этого сохраняем адреса подключённых клиентов
            for (Map.Entry<MessageChannel, Address> entry : connectionMap.entrySet()) {
                MessageChannel channel = entry.getKey();
                Address address = entry.getValue();

                if (address == null) {
                    Message message = channel.poll();
                    if (message != null) {
                        if (message.getClassName().equals(HandshakeDemandMessage.class.getName())) {
                            Address from = message.getFrom();
                            logger.info("Получен запрос на установление соединения от: " + from + ", " + message);
                            connectionMap.put(channel, from);
                            Message handshakeAnswerMessage = new HandshakeAnswerMessage(this.address, from);
                            channel.send(handshakeAnswerMessage);
                            logger.info("Направлен ответ об успешном установлении соединения клиенту: " + from + ", " + handshakeAnswerMessage);
                        }
                    }
                }
            }
            if (isConnectionMapReady()) {
                logger.info("Все связи с клиентами установлены");
                break;  // Когда карта связей заполнена, прерываем цикл
            }
            try {
                Thread.sleep(MESSAGE_DELAY);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }

    private boolean isConnectionMapReady() {
        if (connectionMap.size() == 0) {
            return false;
        }
        for (Map.Entry<MessageChannel, Address> entry : connectionMap.entrySet()) {
            Address address = entry.getValue();
            if (address == null) {
                return false;
            }
        }
        isConnectionMapInitialized = true;

        System.out.println("Connection map is: " + connectionMap);

        return true;
    }

    // Находим по адресу соответствующий ему канал
    private MessageChannel getChannelByAddress(Address address) {
        for (Map.Entry<MessageChannel, Address> entry : connectionMap.entrySet()) {
            if (entry.getValue().equals(address)) {
                return entry.getKey();
            }
        }
        return null;
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

    @Override
    public Address getAddress() {
        return address;
    }
}
