package ru.otus.main;

import ru.otus.collection.MyArrayList;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyArrayList<Integer> myList = new MyArrayList<>();

        myList.add(1);
        myList.add(2);
        myList.add(3);

        printList(myList);

        myList.addAll(1, Arrays.asList(10,11));

        printList(myList);

        myList.listIterator();

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
