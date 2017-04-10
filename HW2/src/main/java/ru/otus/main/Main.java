package ru.otus.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.otus.measure.InstrumentationAgent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, IllegalAccessException, NoSuchFieldException {
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

        Thread.sleep(1 * 1000);

        System.out.println("Start growing up ArrayList...\n");

        int size = 0;
        List<Integer> grownUpList = new ArrayList<>();
        long sumMemory = 0;
        long freeMemoryBefore = 0;
        long freeMemoryAfter = 0;
        long deltaMemory = 0;

        System.out.println(getObjectComplexSize(new int[0]));

        freeMemoryBefore = runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());

        while (size < Integer.MAX_VALUE) {
            size++;
            Integer addInt = new Integer(size);
            sumMemory += getObjectSize(addInt);
            grownUpList.add(addInt);

            if (size % 1_000_000 == 0) {
                Thread.sleep(1000);
                System.out.println("Self object 'grownUpList' size");
                printObjectSizeByte(grownUpList);
                System.out.println("Array from 'grownUpList' size");
                printObjectSizeByte(grownUpList.toArray());
                System.out.println("Array from 'grownUpList' / n = " +
                        getObjectSize(grownUpList.toArray())/size + " bytes\n");
                System.out.println("Count elements of 'grownUpList' = " + size + "\n");
                freeMemoryAfter = runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());
                deltaMemory = freeMemoryBefore - freeMemoryAfter;
                System.out.println("delta memory = " + deltaMemory + " bytes\n");
                System.out.println("delta memory (on 1 element) = " + deltaMemory/size + " bytes\n");
                System.out.println("sum memory of Integer objects = " + sumMemory + " bytes\n");
                System.out.println("----------------\n");
                freeMemoryBefore = freeMemoryAfter;
            }
        }

    }
}

