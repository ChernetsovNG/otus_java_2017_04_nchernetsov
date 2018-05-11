package ru.otus.MyJUnit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import ru.otus.MyJUnit.annotation.After;
import ru.otus.MyJUnit.annotation.Before;
import ru.otus.MyJUnit.annotation.Test;

public class TestMyJUnit {

  // Здесь собственно обрабатываем тестируемый класс, ищем аннотированные методы
  // и выполняем их, проверяя, не было ли брошено исключение
  public static void main(String[] args) {
    // Тестовые методы
    List<Method> beforeMethods = ReflectionHelper
        .getMethodsAnnotatedWith(ClassForMyJUnitTest.class, Before.class);

    List<Method> testMethods = ReflectionHelper
        .getMethodsAnnotatedWith(ClassForMyJUnitTest.class, Test.class);

    List<Method> afterMethods = ReflectionHelper
        .getMethodsAnnotatedWith(ClassForMyJUnitTest.class, After.class);

    // Для каждого тестового метода создаём свой объект
    for (Method testMethod : testMethods) {
      ClassForMyJUnitTest test = new ClassForMyJUnitTest();

      try {
        for (Method beforeMethod : beforeMethods) {
          ReflectionHelper.callMethod(test, beforeMethod.getName());
          System.out.println("Run @Before");
        }

        ReflectionHelper.callMethod(test, testMethod.getName());
        System.out.println("Test method: " + testMethod.getName() + " ok");

        for (Method afterMethod : afterMethods) {
          ReflectionHelper.callMethod(test, afterMethod.getName());
          System.out.println("Run @After");
        }
        System.out.println("Test successfully passed!\n");
      } catch (InvocationTargetException e) {
        e.printStackTrace();
        System.out.println("Test no passed. Something wrong!");
      }
    }
  }

}
