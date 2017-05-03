package ru.otus.MyJUnit.assertion;

public class TestException extends RuntimeException {
    public TestException() {
    }

    public TestException(String message) {
        super(message);
    }
}
