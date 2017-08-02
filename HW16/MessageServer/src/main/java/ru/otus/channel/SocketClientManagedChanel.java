package ru.otus.channel;

import java.io.IOException;
import java.net.Socket;

public class SocketClientManagedChanel extends SocketClientChannel {

    private final Socket socket;

    public SocketClientManagedChanel(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    private SocketClientManagedChanel(Socket socket) throws IOException {
        super(socket);
        this.socket = socket;
    }

    public void close() throws IOException {
        super.close();
        socket.close();
    }
}
