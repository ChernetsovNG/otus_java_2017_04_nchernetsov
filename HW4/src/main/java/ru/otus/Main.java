package ru.otus;

import static ru.otus.MemoryUtil.nOldGarbageCollections;
import static ru.otus.MemoryUtil.nYoungGarbageCollections;
import static ru.otus.MemoryUtil.printCountGarbageCollections;
import static ru.otus.MemoryUtil.printDurationGarbageCollections;
import static ru.otus.MemoryUtil.printUsage;
import static ru.otus.MemoryUtil.startGCMonitor;
import static ru.otus.MemoryUtil.stopGCMonitor;
import static ru.otus.MemoryUtil.timeToOldGarbageCollections;
import static ru.otus.MemoryUtil.timeToYoungGarbageCollections;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
    -agentlib:jdwp=transport=dt_socket,address=14000,server=y,suspend=n
    -Xms512m
    -Xmx512m
    -XX:MaxMetaspaceSize=256m
    -verbose:gc
    -Xloggc:./HW4/logs/gc_pid_%p.log
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
    -XX:OnOutOfMemoryError="kill -3 %p"
    Подключаем разные сборщики мусора:
    -XX:+UseSerialGC
    -XX:+UseParallelGC
    -XX:+UseConcMarkSweepGC
    -XX:+UseG1GC
*/

public class Main {

  public static void main(String[] args) throws InterruptedException, IOException {
    System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

    int n = 0;
    List<Integer> list = new ArrayList<>();
    Random rand = new Random();

    startGCMonitor();  // начинаем наблюдение за памятью
    // installGCMonitoring();
    long startTime = System.currentTimeMillis();

    try {
      while (n < Integer.MAX_VALUE - 10) {
        n++;

        list.add(rand.nextInt(1_000_000));

        if (n % 2 == 0) {
          list.remove(list.size() - 1);
        }

        if (n % 1_000_000 == 0) {
          Thread.sleep(7400);

          System.out.println();
          System.out.println("Add " + n + " objects " + " in list");
          System.out.println("Each second is removed");
          System.out.println("Result size of array: " + list.size());
          System.out.println();

          // печатаем информацию о сборках мусора
          printUsage(true);
          printCountGarbageCollections();
          printDurationGarbageCollections();
        }
      }
    } finally {
      long finishTime = System.currentTimeMillis();
      long workTime = finishTime - startTime;
      long timeToGCWork = timeToYoungGarbageCollections + timeToOldGarbageCollections;

      System.out.println("Общее время работы приложения: " + (double) workTime / 1000 + " s");
      System.out.println("Общее время сборок мусора: " + (double) timeToGCWork / 1000 + " s");
      System.out.println(
          "Общее кол-во сборок мусора: " + (nYoungGarbageCollections + nOldGarbageCollections));
      System.out.println("Из них " + nYoungGarbageCollections + " - в YoungGeneration и");
      System.out.println(nOldGarbageCollections + " - в OldGeneration");

      stopGCMonitor();
    }

  }

}
