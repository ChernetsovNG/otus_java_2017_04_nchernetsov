package ru.otus;

import ru.otus.channel.SocketClientChannel;

import java.io.IOException;
import java.net.Socket;

class SocketClientManagedChanel extends SocketClientChannel {

    private final Socket socket;

    SocketClientManagedChanel(String host, int port) throws IOException {
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
