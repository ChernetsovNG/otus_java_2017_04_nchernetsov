package ru.otus.app;

public abstract class Message {
    public static final String CLASS_NAME_VARIABLE = "className";

    private final String className;

    protected Message(Class<?> klass) {
        this.className = klass.getName();
    }
}
