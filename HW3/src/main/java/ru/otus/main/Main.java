package ru.otus.main;

import ru.otus.collection.MyArrayList;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyArrayList<Number> myList = new MyArrayList<>();

        myList.add(1);
        myList.add(2);
        myList.add(3);
        myList.add(4);
        myList.add(5);

        printList(myList);

        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        list.add(7);
        list.add(8);

        myList.retainAll(list);

        printList(myList);
    }

    public static void printList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("List: ");
        for (int i = 0; i < list.size()-1; i++) {
            sb.append(list.get(i).toString());
            sb.append(", ");
        }
        sb.append(list.get(list.size()-1));
        System.out.println(sb.toString());
    }
}
