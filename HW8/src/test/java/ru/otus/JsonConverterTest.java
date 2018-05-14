package ru.otus;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonConverterTest {
    private static final TestClasses testClasses = new TestClasses();
    private static final Gson gson = new Gson();
    private static final JsonConverter jsonConverterMyImpl = new JsonConverterMyImpl();

    @Test
    public void objectWithPrimitivesTest() {
        TestClasses.ClassWithPrimitives objectWithPrimitives = testClasses.new ClassWithPrimitives();

        String jsonByGson = gson.toJson(objectWithPrimitives);
        String jsonByMyConverter = jsonConverterMyImpl.convertToJSON(objectWithPrimitives);

        System.out.println(jsonByGson);
        System.out.println();

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void objectWithPrimitiveArrayTest() {
        TestClasses.ClassWithPrimitiveArray objectWithPrimitiveArray = testClasses.new ClassWithPrimitiveArray();

        String jsonByGson = gson.toJson(objectWithPrimitiveArray);
        String jsonByMyConverter = jsonConverterMyImpl.convertToJSON(objectWithPrimitiveArray);

        System.out.println(jsonByGson);
        System.out.println();

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void objectWithListOfPrimitivesTest() {
        TestClasses.ClassWithListOfPrimitives objectWithListOfPrimitives = testClasses.new ClassWithListOfPrimitives();

        String jsonByGson = gson.toJson(objectWithListOfPrimitives);
        String jsonByMyConverter = jsonConverterMyImpl.convertToJSON(objectWithListOfPrimitives);

        System.out.println(jsonByGson);
        System.out.println();

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void objectWithSetOfPrimitivesTest() {
        TestClasses.ClassWithSetOfPrimitives objectWithSetOfPrimitives = testClasses.new ClassWithSetOfPrimitives();

        String jsonByGson = gson.toJson(objectWithSetOfPrimitives);
        String jsonByMyConverter = jsonConverterMyImpl.convertToJSON(objectWithSetOfPrimitives);

        System.out.println(jsonByGson);
        System.out.println();

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void objectWithObjectTest() {
        TestClasses.ClassWithObject objectWithObject = testClasses.new ClassWithObject();

        String jsonByGson = gson.toJson(objectWithObject);
        String jsonByMyConverter = jsonConverterMyImpl.convertToJSON(objectWithObject);

        System.out.println(jsonByGson);
        System.out.println();

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void objectWithListOfObjectsTest() {
        TestClasses.ClassWithListOfObjects objectWithListOfObjects = testClasses.new ClassWithListOfObjects();

        String jsonByGson = gson.toJson(objectWithListOfObjects);
        String jsonByMyConverter = jsonConverterMyImpl.convertToJSON(objectWithListOfObjects);

        System.out.println(jsonByGson);
        System.out.println();

        assertEquals(jsonByGson, jsonByMyConverter);
    }

    @Test
    public void objectUserTest() {
        TestClasses.User user = testClasses.new User();

        String jsonByGson = gson.toJson(user);
        String jsonByMyConverter = jsonConverterMyImpl.convertToJSON(user);

        System.out.println(jsonByGson);
        System.out.println();

        assertEquals(jsonByGson, jsonByMyConverter);
    }

}
