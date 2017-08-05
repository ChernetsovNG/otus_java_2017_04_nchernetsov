package ru.otus.app;

import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;

public abstract class Message {
    private final Address from;
    private final Address to;
    private final String className;

    public static final String CLASS_NAME_VARIABLE = "className";

    protected Message(Address from, Address to, Class<?> klass) {
        this.from = from;
        this.to = to;
        this.className = klass.getName();
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public String getClassName() {
        return className;
    }

    public abstract void exec(Addressee addressee);
}
