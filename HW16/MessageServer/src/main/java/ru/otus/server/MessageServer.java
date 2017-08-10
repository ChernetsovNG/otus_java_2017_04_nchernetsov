package ru.otus.server;

import ru.otus.app.Message;
import ru.otus.app.MessageChannel;
import ru.otus.channel.SocketClientChannel;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messages.AddressNotRegisteredMessage;
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
    private static final Logger LOG = Logger.getLogger(MessageServer.class.getName());

    public static final int PORT = 5050;
    private static final int THREADS_NUMBER = 5;
    private static final int MESSAGE_DELAY = 100;

    private final ExecutorService executor;
    private final Map<MessageChannel, Address> connectionMap;  // карта вида <Канал для передачи сообщения - Соответствующий ему адрес>
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

        // Ждём подключения к серверу. Для подключенных серверов создаём каналы для связи
        // и сохраняем эти каналы в карте
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOG.info("Server started on port: " + serverSocket.getLocalPort());

            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept();  // blocks

                LOG.info("Client connect: " + client);
                SocketClientChannel channel = new SocketClientChannel(client);
                channel.init();
                channel.addShutdownRegistration(() -> connectionMap.remove(channel));
                connectionMap.put(channel, null);
            }
        }
    }

    // Принимаем идентифицирующие сообщение, и сохраняем в карте соответствие адресата и его канала
    private void handshake() {
        try {
            // Сохраняем адреса подключённых клиентов
            LOG.info("Начат цикл приёма адресов от клиентов (handshake)...");
            while (true) {
                for (Map.Entry<MessageChannel, Address> entry : connectionMap.entrySet()) {
                    MessageChannel channel = entry.getKey();
                    Address address = entry.getValue();

                    if (address == null) {
                        Message message = channel.poll();
                        if (message != null) {
                            if (message.getClassName().equals(HandshakeDemandMessage.class.getName())) {
                                Address from = message.getFrom();
                                LOG.info("Получен запрос на установление соединения от: " + from + ", " + message);
                                connectionMap.put(channel, from);
                                Message handshakeAnswerMessage = new HandshakeAnswerMessage(this.address, from);
                                channel.send(handshakeAnswerMessage);
                                LOG.info("Направлен ответ об успешном установлении соединения клиенту: " + from + ", " + handshakeAnswerMessage);
                            }
                        }
                    }
                }
                Thread.sleep(MESSAGE_DELAY);
            }
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, e.toString());
        }
    }

    // Основная процедура обработки сообщений. Получает сообщение и пересылает его по нужному адресу,
    // если этот адрес зарегистрирован. Иначе отправляет сообщение о том, что адрес не зарегистирован
    private void messageHandle() {
        try {
            // Запускаем процедуру обработки сообщений
            LOG.info("Начат цикл обработки сообщений");
            while (true) {
                for (Map.Entry<MessageChannel, Address> entry : connectionMap.entrySet()) {
                    MessageChannel channelFrom = entry.getKey();
                    Address addressFrom = entry.getValue();
                    // если соединение с этим клиентом уже было ранее установлено
                    if (addressFrom != null) {
                        Message message = channelFrom.poll();
                        if (message != null) {
                            MessageChannel channelTo = getChannelByAddress(message.getTo());
                            // если адресат уже в карте, то посылаем ему сообщение
                            if (connectionMap.containsKey(channelTo)) {
                                LOG.info("MessageServer receive the message from: " + message.getFrom() + ". Receive it to: " + message.getTo() + ". " + message);
                                channelTo.send(message);
                            } else {  // иначе посылаем обратно сообщение о том, что адресат ещё не был добавлен
                                LOG.info("Адресат для сообщения " + message + " ещё не был добавлен в карту соединений");
                                channelFrom.send(new AddressNotRegisteredMessage(this.getAddress(), message.getFrom()));
                            }
                        }
                    }
                }
                Thread.sleep(MESSAGE_DELAY);
            }
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, e.toString());
        }
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
            LOG.info("Bye.");
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
