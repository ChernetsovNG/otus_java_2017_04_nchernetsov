package ru.otus;

import ru.otus.annotation.Unfinished;

public class TestAnnotatedClass {
    public TestAnnotatedClass() {
    }

    public void method1() {
    }

    @Unfinished("Value1")
    private String method2() {
        return "TestAnnotatedClass.method2 run";
    }
}
