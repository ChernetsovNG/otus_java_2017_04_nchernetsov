package ru.otus;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConvertToJsonTest {
    private static final TestClasses testClasses = new TestClasses();
    private static final Gson gson = new Gson();
    private static final JsonConverter jsonConverter = new JsonConverter();

    @Test
    public void objectWithPrimitivesTest() {
        TestClasses.ClassWithPrimitives objectWithPrimitives = testClasses.new ClassWithPrimitives();

        String jsonByGson = gson.toJson(objectWithPrimitives);
        String jsonByMyConverter = jsonConverter.objectToJson(objectWithPrimitives);

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void objectWithPrimitiveArrayTest() {
        TestClasses.ClassWithPrimitiveArray objectWithPrimitiveArray = testClasses.new ClassWithPrimitiveArray();

        String jsonByGson = gson.toJson(objectWithPrimitiveArray);
        String jsonByMyConverter = jsonConverter.objectToJson(objectWithPrimitiveArray);

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void GsonTest() {

        TestClasses.ClassWithListOfPrimitives objectWithListOfPrimitives = testClasses.new ClassWithListOfPrimitives();
        TestClasses.ClassWithSetOfPrimitives objectWithSetOfPrimitives = testClasses.new ClassWithSetOfPrimitives();
        TestClasses.ClassWithObject objectWithObject = testClasses.new ClassWithObject();
        TestClasses.ClassWithListOfObjects objectWithListOfObjects = testClasses.new ClassWithListOfObjects();


        String jsonClassWithListOfPrimitives = gson.toJson(objectWithListOfPrimitives);
        String jsonClassWithSetOfPrimitives = gson.toJson(objectWithSetOfPrimitives);
        String jsonClassWithObject = gson.toJson(objectWithObject);
        String jsonClassWithListOfObjects = gson.toJson(objectWithListOfObjects);


        System.out.println(jsonClassWithListOfPrimitives);
        System.out.println(jsonClassWithSetOfPrimitives);
        System.out.println(jsonClassWithObject);
        System.out.println(jsonClassWithListOfObjects);
    }
}