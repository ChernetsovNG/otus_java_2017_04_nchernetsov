package ru.otus.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static ru.otus.measure.InstrumentationAgent.printObjectSizeMb;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName() + "/n");

        //TestData testData = new TestData();
        //testData.printObjectsSize();

        int size = 50 * 1024 * 1024;
        Object[] array = new Object[size];
        System.out.println("Array of size: " + array.length + " created");
        printObjectSizeMb(array);

        Thread.sleep(1 * 1000);

        int n = 0;
        System.out.println("Starting the loop\n");
        while (n < Integer.MAX_VALUE) {
            int i = n % size;
            array[i] = new String(""); //no String pool
            n++;
            /*if (n % 1024 == 0) {
                Thread.sleep(1);
            }*/
            if (n % size == 0) {
                System.out.println("Created " + n + " objects");
                System.out.println("Creating new array");
                array = new Object[size];
                printObjectSizeMb(array);
            }
        }

    }
}

