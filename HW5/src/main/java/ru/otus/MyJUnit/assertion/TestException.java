package ru.otus.MyJUnit.assertion;

public class TestException extends Error {
    public TestException() {
    }

    public TestException(String message) {
        super(message);
    }
}
