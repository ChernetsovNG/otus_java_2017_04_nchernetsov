package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

// https://habrahabr.ru/post/269621/
public class MemoryUtil {

  private static final int NORM_NAME_LENGTH = 25;
  private static final long SIZE_KB = 1024;
  private static final long SIZE_MB = SIZE_KB * 1024;
  private static final long SIZE_GB = SIZE_MB * 1024;
  private static final String SPACES = "                     ";
  private static Map<String, MemRegion> memRegions;

  public static int nYoungGarbageCollections = 0;
  public static int nOldGarbageCollections = 0;
  public static long timeToYoungGarbageCollections = 0;
  public static long timeToOldGarbageCollections = 0;

  // Вспомогательный класс для хранения информации о регионах памяти
  private static class MemRegion {

    private boolean heap;        // Признак того, что это регион кучи
    private String normName;     // Имя, доведенное пробелами до универсальной длины

    MemRegion(String name, boolean heap) {
      this.heap = heap;
      normName = name.length() < NORM_NAME_LENGTH ? name
          .concat(SPACES.substring(0, NORM_NAME_LENGTH - name.length())) : name;
    }

    boolean isHeap() {
      return heap;
    }

    String getNormName() {
      return normName;
    }
  }

  static {
    // Запоминаем информацию обо всех регионах памяти
    memRegions = new HashMap<>(ManagementFactory.getMemoryPoolMXBeans().size());
    for (MemoryPoolMXBean mBean : ManagementFactory.getMemoryPoolMXBeans()) {
      memRegions
          .put(mBean.getName(), new MemRegion(mBean.getName(), mBean.getType() == MemoryType.HEAP));
    }
  }

  // Обработчик сообщений о сборке мусора
  private static NotificationListener gcHandler = (notification, handback) -> {
    if (notification.getType()
        .equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
      GarbageCollectionNotificationInfo gcInfo = GarbageCollectionNotificationInfo
          .from((CompositeData) notification.getUserData());
      Map<String, MemoryUsage> memBefore = gcInfo.getGcInfo().getMemoryUsageBeforeGc();
      Map<String, MemoryUsage> memAfter = gcInfo.getGcInfo().getMemoryUsageAfterGc();

      String gcType = gcInfo.getGcAction();
      long duration = gcInfo.getGcInfo().getDuration();

      StringBuilder sb = new StringBuilder();
      sb.append("[").append(gcType).append(" / ").append(gcInfo.getGcCause())
          .append(" / ").append(gcInfo.getGcName()).append(" / (");
      appendMemUsage(sb, memBefore);
      sb.append(") -> (");
      appendMemUsage(sb, memAfter);
      sb.append("), ").append(duration).append(" ms]");
      System.out.println(sb.toString());

      // Подсчитываем количество сборок каждого типа и суммарное время на них
      if (gcType.contains("minor")) {
        nYoungGarbageCollections++;
        timeToYoungGarbageCollections += duration;
      } else if (gcType.contains("major")) {
        nOldGarbageCollections++;
        timeToOldGarbageCollections += duration;
      }
    }
  };

  /**
   * Выводит в stdout информацию о текущем состоянии различных разделов памяти
   */
  public static void printUsage(boolean heapOnly) {
    for (MemoryPoolMXBean mBean : ManagementFactory.getMemoryPoolMXBeans()) {
      if (!heapOnly || mBean.getType() == MemoryType.HEAP) {
        printMemUsage(mBean.getName(), mBean.getUsage());
      }
    }
  }

  /**
   * Запускает процесс мониторинга сборок мусора
   */
  public static void startGCMonitor() {
    for (GarbageCollectorMXBean mBean : ManagementFactory.getGarbageCollectorMXBeans()) {
      ((NotificationEmitter) mBean).addNotificationListener(gcHandler, null, null);
    }
  }

  /**
   * Останавливает процесс мониторинга сборок мусора
   */
  public static void stopGCMonitor() {
    for (GarbageCollectorMXBean mBean : ManagementFactory.getGarbageCollectorMXBeans()) {
      try {
        ((NotificationEmitter) mBean).removeNotificationListener(gcHandler);
      } catch (ListenerNotFoundException ignored) {
      }
    }
  }

  private static void printMemUsage(String title, MemoryUsage usage) {
    System.out.println(String.format("%s%s\t%.1f%%\t[%s]",
        memRegions.get(title).getNormName(),
        formatMemory(usage.getUsed()),
        usage.getMax() < 0 ? 0.0 : (double) usage.getUsed() / (double) usage.getMax() * 100,
        formatMemory(usage.getMax())));
  }

  private static String formatMemory(long bytes) {
    if (bytes > SIZE_GB) {
      return String.format("%.2fG", bytes / (double) SIZE_GB);
    } else if (bytes > SIZE_MB) {
      return String.format("%.2fM", bytes / (double) SIZE_MB);
    } else if (bytes > SIZE_KB) {
      return String.format("%.2fK", bytes / (double) SIZE_KB);
    }
    return Long.toString(bytes);
  }

  private static void appendMemUsage(StringBuilder sb, Map<String, MemoryUsage> memUsage) {
    for (Map.Entry<String, MemoryUsage> entry : memUsage.entrySet()) {
      if (memRegions.get(entry.getKey()).isHeap()) {
        sb.append(entry.getKey()).append(" used=")
            .append(entry.getValue().getUsed() >> 10)
            .append("K; ");
      }
    }
  }

  public static void printCountGarbageCollections() {
    System.out.println(
        "Count of garbage collections of young generation objects: " + nYoungGarbageCollections);
    System.out.println(
        "Count of garbage collections of old generation objects: " + nOldGarbageCollections);
  }

  public static void printDurationGarbageCollections() {
    System.out.println("Duration of garbage collections of young generation objects: "
        + timeToYoungGarbageCollections + " ms");
    System.out.println(
        "Duration of garbage collections of old generation objects: " + timeToOldGarbageCollections
            + " ms");
  }

  // Взято из примера L2.1.2 В.Чибрикова
  public static void installGCMonitoring() {
    List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory
        .getGarbageCollectorMXBeans();
    for (GarbageCollectorMXBean gcbean : gcbeans) {
      NotificationEmitter emitter = (NotificationEmitter) gcbean;
      System.out.println(gcbean.getName());

      NotificationListener listener = (notification, handback) -> {
        if (notification.getType()
            .equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
          GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
              .from((CompositeData) notification.getUserData());

          long duration = info.getGcInfo().getDuration();
          String gctype = info.getGcAction();

          System.out.println(gctype + ": - "
              + info.getGcInfo().getId() + ", "
              + info.getGcName()
              + " (from " + info.getGcCause() + ") " + duration + " milliseconds");

        }
      };

      emitter.addNotificationListener(listener, null, null);
    }
  }

}
