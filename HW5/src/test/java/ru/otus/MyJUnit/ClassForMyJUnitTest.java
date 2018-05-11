package ru.otus.MyJUnit;

import java.util.ArrayList;
import java.util.List;
import ru.otus.MyJUnit.annotation.After;
import ru.otus.MyJUnit.annotation.Before;
import ru.otus.MyJUnit.annotation.Test;
import ru.otus.MyJUnit.assertion.Assert;

//Класс, при помощи которого тестируем MyJUnit
public class ClassForMyJUnitTest {

  public static List<Integer> list = null;

  @Before
  public void beforeTest() {
    list = new ArrayList<>();
    list.add(1);
    list.add(2);
  }

  @Test
  public void ariphmeticTest() {
    int a = list.get(0) + list.get(1);
    Assert.assertEquals(3, a);
  }

  @Test
  public void booleanTest() {
    boolean bool = (5 >= 3);
    Assert.assertTrue(bool);
  }

  @Test
  public void notNullTest() {
    Assert.assertNotNull(new Object());
  }

  @After
  public void afterTest() {
    list = null;
  }

}
