package ru.otus.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static ru.otus.measure.InstrumentationAgent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, IllegalAccessException {
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

        Thread.sleep(1 * 1000);

        System.out.println("Start growing up ArrayList...\n");

        int size = 0;
        long memory = 0;
        List<Integer> grownUpList = new ArrayList<>();

        while (size < Integer.MAX_VALUE) {
            size++;
            Integer addInt = new Integer(size);
            grownUpList.add(addInt);

            if (size % 1_000_000 == 0) {
                Thread.sleep(1000);
                memory = getObjectComplexSize(grownUpList);
                System.out.println("grownUpList memory size = " + memory + " bytes\n");
                System.out.println("Count elements of 'grownUpList' = " + size + "\n");
                System.out.println("Memory on 1 element = " + memory/size + " bytes\n");
                System.out.println("----------------\n");
            }
        }

    }
}

