package ru.otus.app;

import java.io.IOException;

public interface MessageChannel {
    void send(Message message);

    Message poll();

    Message take() throws InterruptedException;

    void close() throws IOException;
}
