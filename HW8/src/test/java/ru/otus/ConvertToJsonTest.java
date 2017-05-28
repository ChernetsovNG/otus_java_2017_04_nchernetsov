package ru.otus;

import com.google.gson.Gson;
import org.junit.Test;

public class ConvertToJsonTest {
    @Test
    public void GsonTest() {
        TestClasses testClasses = new TestClasses();

        TestClasses.ClassWithPrimitives objectWithPrimitives = testClasses.new ClassWithPrimitives();
        TestClasses.ClassWithPrimitiveArray objectWithPrimitiveArray = testClasses.new ClassWithPrimitiveArray();
        TestClasses.ClassWithListOfPrimitives objectWithListOfPrimitives = testClasses.new ClassWithListOfPrimitives();
        TestClasses.ClassWithSetOfPrimitives objectWithSetOfPrimitives = testClasses.new ClassWithSetOfPrimitives();
        TestClasses.ClassWithObject objectWithObject = testClasses.new ClassWithObject();
        TestClasses.ClassWithListOfObjects objectWithListOfObjects = testClasses.new ClassWithListOfObjects();

        Gson gson = new Gson();

        String jsonClassWithPrimitives = gson.toJson(objectWithPrimitives);
        String jsonClassWithPrimitiveArray = gson.toJson(objectWithPrimitiveArray);
        String jsonClassWithListOfPrimitives = gson.toJson(objectWithListOfPrimitives);
        String jsonClassWithSetOfPrimitives = gson.toJson(objectWithSetOfPrimitives);
        String jsonClassWithObject = gson.toJson(objectWithObject);
        String jsonClassWithListOfObjects = gson.toJson(objectWithListOfObjects);

        System.out.println(jsonClassWithPrimitives);
        System.out.println(jsonClassWithPrimitiveArray);
        System.out.println(jsonClassWithListOfPrimitives);
        System.out.println(jsonClassWithSetOfPrimitives);
        System.out.println(jsonClassWithObject);
        System.out.println(jsonClassWithListOfObjects);
    }
}