package ru.otus.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.otus.measure.InstrumentationAgent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName() + "\n");

        //в тестовых данных рассмотрены разные типы
        //TestData testData = new TestData();
        //testData.printObjectsSize();

        printObjectSizeByte(new Object());
        printObjectSizeByte(new String(""));
        printObjectSizeByte(new ArrayList<Integer>());
        printObjectSizeByte(new Integer(1));
        printObjectSizeByte(new HashMap<>());
        String str = new String("");
        System.out.println(getSomeObjectsSize(str, str.toCharArray()) + " bytes\n");

        Thread.sleep(3 * 1000);

        System.out.println("Start growing up ArrayList...\n");

        int size = 0;
        List<Integer> grownUpList = new ArrayList<>();
        long sumMemory = 0;
        long allocatedMemory = 0;

        while (size < Integer.MAX_VALUE) {
            size++;
            Integer addInt = new Integer(size);
            sumMemory += getObjectSize(addInt);
            grownUpList.add(addInt);

            Thread.sleep(1);
            if (size % 1000 == 0) {
                System.out.println("Self object 'grownUpList' size");
                printObjectSizeByte(grownUpList);
                System.out.println("Array from 'grownUpList' size");
                printObjectSizeByte(grownUpList.toArray());
                System.out.println("Array from 'grownUpList' / n = " +
                        getObjectSize(grownUpList.toArray())/size + " bytes\n");
                System.out.println("Count elements of 'grownUpList' = " + size + "\n");
                allocatedMemory = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("allocated memory = " + allocatedMemory + " bytes\n");
                System.out.println("allocated memory (on 1 element) = " + allocatedMemory/size + " bytes\n");
                System.out.println("sum memory of Integer objects = " + sumMemory + " bytes\n");
                System.out.println("----------------\n");
            }
        }

    }
}

