package ru.otus.app;

import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;

public abstract class Message {
    private final Address from;
    private final Address to;
    private final String className;
    private final String payload;  // "полезная нагрузка", т.е. само содержание сообщения

    public static final String CLASS_NAME_VARIABLE = "className";

    protected Message(Address from, Address to, String payload, Class<?> klass) {
        this.from = from;
        this.to = to;
        this.payload = payload;
        this.className = klass.getName();
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public String getPayload() {
        return payload;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "Message{" +
            "from=" + from +
            ", to=" + to +
            ", className='" + className + '\'' +
            ", payload='" + payload + '\'' +
            '}';
    }
}
