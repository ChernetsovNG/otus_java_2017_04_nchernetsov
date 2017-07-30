package ru.otus.nchernetsov.app;

import java.io.IOException;

public interface MsgChannel {
    void send(Msg msg);

    Msg pool();

    Msg take() throws InterruptedException;

    void close() throws IOException;
}
