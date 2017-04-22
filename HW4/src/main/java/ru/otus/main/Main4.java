package ru.otus.main;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.otus.utils.MemoryUtil.*;

/* -agentlib:jdwp=transport=dt_socket,address=14000,server=y,suspend=n
         -Xms512m
         -Xmx512m
         -XX:MaxMetaspaceSize=256m
         -XX:+UseConcMarkSweepGC
         -XX:+CMSParallelRemarkEnabled
         -XX:+UseCMSInitiatingOccupancyOnly
         -XX:CMSInitiatingOccupancyFraction=70
         -XX:+ScavengeBeforeFullGC
         -XX:+CMSScavengeBeforeRemark
         -XX:+UseParNewGC
         -verbose:gc
         -Xloggc:.HW4/logs/gc_pid_%p.log
         -XX:+PrintGCDateStamps
         -XX:+PrintGCDetails
         -XX:+UseGCLogFileRotation
         -XX:NumberOfGCLogFiles=10
         -XX:GCLogFileSize=1M
         -Dcom.sun.management.jmxremote.port=15000
         -Dcom.sun.management.jmxremote.authenticate=false
         -Dcom.sun.management.jmxremote.ssl=false
         -XX:+HeapDumpOnOutOfMemoryError
         -XX:HeapDumpPath=./dumps/
         -XX:OnOutOfMemoryError="kill -3 %p"*/

public class Main4 {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        int size = 5 * 1000 * 1000;

        Object[] array = new Object[size];
        System.out.println("Array of size: " + array.length + " created");

        int n = 0;
        int currentSize = size;
        System.out.println("Starting the loop");

        startGCMonitor();
        //installGCMonitoring();

        while (n < Integer.MAX_VALUE) {
            int i = n % currentSize;
            array[i] = new String(new char[0]);
            n++;
            if (n % currentSize == 0) {
                Thread.sleep(1000);
                System.out.println("Created " + n + " objects");
                System.out.println("Creating new array of size: " + size);
                printUsage(true);
                printCountGarbageCollections();
                printDurationGarbageCollections();
                currentSize = size;
                array = new Object[currentSize];
            }
        }

        stopGCMonitor();

    }

}
