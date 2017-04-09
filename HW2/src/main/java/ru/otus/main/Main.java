package ru.otus.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.otus.measure.InstrumentationAgent.printObjectSizeByte;
import static ru.otus.measure.InstrumentationAgent.printObjectSizeMb;

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

        Thread.sleep(3 * 1000);

        System.out.println("Start growing up ArrayList...\n");

        int size = 0;
        List<Integer> grownUpList = new ArrayList<>();
        long allocatedMemory;

        while (size < Integer.MAX_VALUE) {
            size++;
            grownUpList.add(1);
            Thread.sleep(1);
            if (size % 1000 == 0) {
                printObjectSizeByte(grownUpList);
                System.out.println("size = " + size + "\n");
                allocatedMemory = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("allocated memory = " + allocatedMemory + " bytes\n");
                System.out.println("allocated memory (on 1 element) = " + allocatedMemory/size + " bytes\n");
                System.out.println("----------------\n");
            }
        }

    }
}

