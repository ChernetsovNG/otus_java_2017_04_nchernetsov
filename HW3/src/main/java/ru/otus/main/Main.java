package ru.otus.main;

import ru.otus.collection.MyArrayList;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Float> listFloat = new ArrayList<>(2);
        listFloat.add(3.2f);
        listFloat.add(5.5f);

        MyArrayList<Number> myList = new MyArrayList<>(listFloat);
        myList.add(3.3f);
        myList.remove(5.5f);
    }
}
