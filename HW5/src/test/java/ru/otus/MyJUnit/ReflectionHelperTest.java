package ru.otus.MyJUnit;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.MyJUnit.annotation.MyTest;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectionHelperTest {
    @SuppressWarnings("ConstantConditions")
    @Test
    public void instantiate() {
        Assert.assertEquals(0, ReflectionHelper.instantiate(TestClass.class).getA());
        Assert.assertEquals(1, ReflectionHelper.instantiate(TestClass.class, 1).getA());
        Assert.assertEquals("A", ReflectionHelper.instantiate(TestClass.class, 1, "A").getS());
    }

    @Test
    public void getFieldValue() {
        Assert.assertEquals("A", ReflectionHelper.getFieldValue(new TestClass(1, "A"), "s"));
        Assert.assertEquals(1, ReflectionHelper.getFieldValue(new TestClass(1, "B"), "a"));
    }

    @Test
    public void setFieldValue() {
        TestClass test = new TestClass(1, "A");
        Assert.assertEquals("A", test.getS());
        ReflectionHelper.setFieldValue(test, "s", "B");
        Assert.assertEquals("B", test.getS());
    }

    @Test
    public void callMethod() {
        Assert.assertEquals("A", ReflectionHelper.callMethod(new TestClass(1, "A"), "getS"));

        TestClass test = new TestClass(1, "A");
        ReflectionHelper.callMethod(test, "setDefault");
        Assert.assertEquals("", test.getS());
    }

    @Test
    public void getMethodsAnnotatedWith() {
        TestAnnotatedClass test = new TestAnnotatedClass();
        List<Method> methods = ReflectionHelper.getMethodsAnnotatedWith(test.getClass(), MyTest.class);
        Assert.assertEquals(methods.get(0).getName(), "method2");
    }

    @Test
    public void getAndRunMethodsAnnotatedWith() {
        TestAnnotatedClass test = new TestAnnotatedClass();
        List<Method> methods = ReflectionHelper.getMethodsAnnotatedWith(test.getClass(), MyTest.class);

        for (Method method : methods) {
            String s = (String) ReflectionHelper.callMethod(test, method.getName());
            Assert.assertEquals(s, "TestAnnotatedClass.method2 run");
        }
    }

}
