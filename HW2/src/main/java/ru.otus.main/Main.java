package ru.otus.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Main {
    int bitInByte = 8;
    int ByteInKb = 1024;
    int byteInMb = ByteInKb * 1024;

    public static void main(String[] args) throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("Memory: " + (runtime.totalMemory() - runtime.freeMemory()));

        TestData testData = new TestData();
        testData.printObjectsSize();
    }
}

