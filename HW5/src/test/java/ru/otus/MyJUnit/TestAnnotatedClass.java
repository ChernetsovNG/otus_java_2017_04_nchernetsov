package ru.otus.MyJUnit;

import ru.otus.MyJUnit.annotation.MyTest;

public class TestAnnotatedClass {
    public TestAnnotatedClass() {
    }

    public void method1() {
    }

    @MyTest
    private String method2() {
        return "TestAnnotatedClass.method2 run";
    }
}
