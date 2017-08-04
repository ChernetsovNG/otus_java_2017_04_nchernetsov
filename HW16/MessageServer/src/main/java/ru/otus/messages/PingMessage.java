package ru.otus.messages;

import ru.otus.app.Message;

public class PingMessage extends Message {
    private final long time;

    public PingMessage() {
        super(PingMessage.class);
        this.time = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "PingMessage{" +
            "time=" + time +
            "} " + super.toString();
    }
}
