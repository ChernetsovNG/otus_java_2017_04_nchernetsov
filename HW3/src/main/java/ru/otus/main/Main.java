package ru.otus.main;

import ru.otus.collection.MyArrayList;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Float> listFloat = new ArrayList<>(2);
        listFloat.add(3.2f);
        listFloat.add(5.5f);

        List<String> listString = new ArrayList<>(2);
        listString.add("Hello");
        listString.add("world");

        MyArrayList<Number> myList = new MyArrayList<>(listFloat);
        myList.addAll(listFloat);
        myList.add(3.3f);
        myList.remove(5.5f);
    }
}
