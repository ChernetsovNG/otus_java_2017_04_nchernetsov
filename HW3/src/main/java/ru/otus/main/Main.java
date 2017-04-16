package ru.otus.main;

import ru.otus.collection.MyArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.otus.util.Utils.printList;

public class Main {
    public static void main(String[] args) {
        //Тестируем созданный класс
        //1. Конструкторы
        System.out.println("Конструкторы");

        List<Integer> myList1 = new MyArrayList<>();
        printList(myList1);

        List<Integer> myList2 = new MyArrayList<>(5);
        printList(myList2);

        List<Integer> myList3 = new MyArrayList<>(Arrays.asList(1,2,3,4,5));
        printList(myList3);
        System.out.println("");

        //2. Метод size()
        System.out.println("Метод size()");
        System.out.println(myList1.size());
        System.out.println(myList3.size());
        System.out.println("");

        //3. Метод isEmpty()
        System.out.println("Метод isEmpty()");
        System.out.println(myList1.isEmpty());
        System.out.println(myList3.isEmpty());
        System.out.println("");

        //4. Метод contains()
        System.out.println("Метод contains");
        System.out.println(myList1.contains(3));
        System.out.println(myList3.contains(3));
        System.out.println("");

        //5. Метод toArray()
        System.out.println("Метод toArray");
        System.out.println(myList1.toArray());
        System.out.println(myList3.toArray());
        System.out.println("");
    }

}
