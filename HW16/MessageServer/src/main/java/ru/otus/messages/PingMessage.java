package ru.otus.messages;

import ru.otus.app.Message;

public class PingMessage extends Message {
    private final long time;
    private final String from;

    public PingMessage(String from) {
        super(PingMessage.class);
        this.time = System.currentTimeMillis();
        this.from = from;
    }

    public long getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "PingMessage{" +
            "time=" + time +
            "} " + super.toString();
    }
}
