package ru.otus;

import com.google.gson.Gson;
import org.junit.Test;

public class ConvertToJson {
    @Test
    public void GsonTest() {
        TestUtils.BagOfPrimitives bagOfPrimitives = new TestUtils().new BagOfPrimitives();

        System.out.println(bagOfPrimitives);
        Gson gson = new Gson();
        String json = gson.toJson(bagOfPrimitives);
        System.out.println(json);
    }
}