package ru.otus.main;

import java.lang.management.ManagementFactory;

import static ru.otus.utils.MemoryUtil.*;

public class Main4 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        int size = 5 * 1000 * 1000;

        Object[] array = new Object[size];
        System.out.println("Array of size: " + array.length + " created");

        int n = 0;
        int currentSize = size;
        System.out.println("Starting the loop");

        //startGCMonitor();
        installGCMonitoring();

        while (n < Integer.MAX_VALUE) {
            int i = n % currentSize;
            array[i] = new String(new char[0]);
            n++;
            if (n % currentSize == 0) {
                Thread.sleep(1000);
                System.out.println("Created " + n + " objects");
                System.out.println("Creating new array of size: " + size);
                printUsage(true);
                currentSize = size;
                array = new Object[currentSize];
            }
        }

        //stopGCMonitor();

    }

}
