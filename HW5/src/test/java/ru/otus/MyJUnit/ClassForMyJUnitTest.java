package ru.otus.MyJUnit;

import ru.otus.MyJUnit.annotation.MyAfter;
import ru.otus.MyJUnit.annotation.MyBefore;
import ru.otus.MyJUnit.annotation.MyTest;
import ru.otus.MyJUnit.assertion.MyAssert;

import java.util.ArrayList;
import java.util.List;

//Класс, при помощи которого тестируем MyJUnit
public class ClassForMyJUnitTest {
    public static List<Integer> list = null;

    @MyBefore
    public void beforeTest() {
        list = new ArrayList<>();
        list.add(1);
        list.add(2);
    }

    @MyTest
    public void ariphmeticTest() {
        int a = list.get(0) + list.get(1);
        MyAssert.assertEquals(3, a );
    }

    @MyTest
    public void booleanTest() {
        boolean bool = (5 >= 3);
        MyAssert.assertTrue(bool);
    }

    @MyTest
    public void notNullTest() {
        MyAssert.assertNotNull(new Object());
    }

    @MyAfter
    public void afterTest() {
        list = null;
    }

}
