package ru.otus.main;

import ru.otus.collection.MyArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.otus.util.Utils.printList;

public class Main3 {
    public static void main(String[] args) {
        //Тестируем созданный класс
        //1. Конструкторы
        System.out.println("1. Конструкторы");

        List<Integer> myList1 = new MyArrayList<>();
        printList(myList1);

        List<Integer> myList2 = new MyArrayList<>(5);
        printList(myList2);

        List<Integer> myList3 = new MyArrayList<>(Arrays.asList(1,2,3,4,5));
        printList(myList3);
        System.out.println("");

        //2. Метод size()
        System.out.println("2. Метод size()");
        System.out.println(myList1.size());
        System.out.println(myList3.size());
        System.out.println("");

        //3. Метод isEmpty()
        System.out.println("3. Метод isEmpty()");
        System.out.println(myList1.isEmpty());
        System.out.println(myList3.isEmpty());
        System.out.println("");

        //4. Метод contains()
        System.out.println("4. Метод contains");
        System.out.println(myList1.contains(3));
        System.out.println(myList3.contains(3));
        System.out.println("");

        //5. Метод toArray()
        System.out.println("5. Метод toArray");
        System.out.println(myList1.toArray());
        System.out.println(myList3.toArray());
        System.out.println("");

        //6. Метод add()
        System.out.println("6. Метод add");
        myList1.add(10);
        myList1.add(15);
        myList1.add(20);
        printList(myList1);
        System.out.println("");

        //7. Метод remove(Object o)
        System.out.println("7. Метод remove(Object o)");
        myList1.remove(new Integer(12));
        myList1.remove(new Integer(15));
        printList(myList1);
        System.out.println("");

        //8. Метод containsAll()
        System.out.println("8. Метод containsAll");
        System.out.println(myList1.containsAll(Arrays.asList(10, 20, 30)));
        System.out.println(myList1.containsAll(Arrays.asList(10, 20)));
        System.out.println("");

        //9. Метод addAll()
        System.out.println("9. Метод addAll");
        myList1.addAll(Arrays.asList(20, 30, 40, 50));
        printList(myList1);
        System.out.println("");

        //10. Метод addAll(index, collection)
        System.out.println("10. Метод addAll(index, collection)");
        myList3.addAll(2, Arrays.asList(10, 11, 12));
        printList(myList3);
        System.out.println("");

        //11. Метод removeAll
        System.out.println("11. Метод removeAll");
        myList1.removeAll(Arrays.asList(20, 50));
        printList(myList1);
        System.out.println("");

        //12. Метод retainAll
        System.out.println("12. Метод retainAll");
        myList1.retainAll(Arrays.asList(10, 20));
        printList(myList1);
        System.out.println("");

        //13. Метод sort
        System.out.println("13. Метод sort");
        myList3.sort(Integer::compareTo);
        printList(myList3);
        System.out.println("");

        //14. Метод clear
        System.out.println("14. Метод clear");
        myList1.clear();
        myList2.clear();
        myList3.clear();
        printList(myList1);
        printList(myList2);
        printList(myList3);
        System.out.println("");

        //15. Метод get
        List<Integer> myList4 = new MyArrayList<>();

        System.out.println("15. Метод get");
        myList4.addAll(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println(myList4.get(3));
        System.out.println("");

        //16. Метод set
        System.out.println("16. Метод set");
        myList4.set(2, new Integer(7));
        printList(myList4);
        System.out.println("");

        //17. Метод add(index)
        System.out.println("17. Метод add(index)");
        myList4.add(4, new Integer(8));
        printList(myList4);
        System.out.println("");

        //18. Метод remove(index)
        System.out.println("18. Метод remove(index)");
        myList4.remove(3);
        printList(myList4);
        System.out.println("");

        //19. Метод indexOf
        System.out.println("19. Метод indexOf");
        System.out.println(myList4.indexOf(7));
        System.out.println("");

        //20. Метод lastIndexOf
        System.out.println("20. Метод lastIndexOf");
        myList4.add(7);
        System.out.println(myList4.lastIndexOf(7));
        System.out.println("");

        //21. Метод iterator()
        System.out.println("21. Метод iterator()");
        for (Integer integer : myList4) {
            System.out.println(integer);
        }
        System.out.println("");

        //22. Метод Collections.addAll
        System.out.println("22. Метод Collections.addAll");
        Collections.addAll(myList4, 10,11,12);
        printList(myList4);
        System.out.println("");

        //23. Метод Collections.copy
        System.out.println("23. Метод Collections.copy");
        List<Integer> src = new ArrayList<>();
        src.add(20);
        src.add(30);
        Collections.copy(myList4, src);
        printList(myList4);
        System.out.println("");

        //24. Метод Collections.sort
        System.out.println("24. Метод Collections.sort");
        Collections.sort(myList4, Integer::compareTo);
        printList(myList4);
        System.out.println("");
    }

}
