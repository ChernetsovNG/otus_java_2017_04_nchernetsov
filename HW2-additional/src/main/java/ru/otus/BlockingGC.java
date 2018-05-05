package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION;

class BlockingGC {
    private final CountDownLatch doneSignal = new CountDownLatch(2);  // 2 сборщика мусора
    private List<Runnable> registrations = new ArrayList<>();

    private BlockingGC() {
        installGCMonitoring();
    }

    static void collect() {
        BlockingGC blockingGC = new BlockingGC();
        try {
            System.gc();
            blockingGC.doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            blockingGC.registrations.forEach(Runnable::run);
        }
    }

    private void installGCMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    long duration = info.getGcInfo().getDuration();
                    String gcType = info.getGcAction();

                    System.out.println(gcType + ": - " +
                        info.getGcInfo().getId() + " " +
                        info.getGcName() +
                        " (from " + info.getGcCause() + ") " + duration + " ms");

                    if (info.getGcCause().equals("System.gc()")) {
                        doneSignal.countDown();
                    }
                }
            };

            emitter.addNotificationListener(listener, null, null);

            registrations.add(() -> {
                try {
                    emitter.removeNotificationListener(listener);
                } catch (ListenerNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
