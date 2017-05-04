package ru.otus.MyJUnit;

import ru.otus.MyJUnit.annotation.MyAfter;
import ru.otus.MyJUnit.annotation.MyBefore;
import ru.otus.MyJUnit.annotation.MyTest;
import ru.otus.MyJUnit.assertion.TestException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TestMyJUnit {
    //Здесь собственно обрабатываем тестируемый класс, ищем аннотированные методы
    //и выполняем их, проверяя, не было ли брошено исключение
    public static void main(String[] args) {
        ClassForMyJUnitTest test = new ClassForMyJUnitTest();

        List<Method> beforeMethods = ReflectionHelper.getMethodsAnnotatedWith(test.getClass(), MyBefore.class);
        List<Method> testMethods = ReflectionHelper.getMethodsAnnotatedWith(test.getClass(), MyTest.class);
        List<Method> afterMethods = ReflectionHelper.getMethodsAnnotatedWith(test.getClass(), MyAfter.class);

        try {
            for (Method beforeMethod : beforeMethods) {
                ReflectionHelper.callMethod(test, beforeMethod.getName());
            }
            for (Method testMethod : testMethods) {
                ReflectionHelper.callMethod(test, testMethod.getName());
                System.out.println("Test method: " + testMethod.getName() + " ok");
            }
            for (Method afterMethod : afterMethods) {
                ReflectionHelper.callMethod(test, afterMethod.getName());
            }
            System.out.println("All tests successfully passed!");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("Not all tests passed. Something wrong!");
        }

    }
}






