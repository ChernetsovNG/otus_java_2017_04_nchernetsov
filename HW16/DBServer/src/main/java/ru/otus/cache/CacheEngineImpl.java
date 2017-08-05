package ru.otus.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<Element<K, V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public void put(Element<K, V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        K key = element.getKey();
        elements.put(key, new SoftReference<>(element));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs);
            }
        }
    }

    @Override
    public Element<K, V> get(K key) {
        SoftReference<Element<K, V>> elementSoftReference = elements.get(key);

        Element<K, V> element;
        if (elementSoftReference != null) {
            element = elementSoftReference.get();
        } else {
            element = null;
        }

        if (element != null) {
            hit++;
            element.setAccessed();
        } else {
            miss++;
        }

        return element;
    }

    @Override
    public List<Element<K, V>> getAll() {
        List<Element<K, V>> result = new ArrayList<>();

        Collection<SoftReference<Element<K, V>>> elementSoftReferences = elements.values();

        for (SoftReference<Element<K, V>> elementSoftReference : elementSoftReferences) {
            Element<K, V> element;
            if (elementSoftReference != null) {
                element = elementSoftReference.get();
            } else {
                element = null;
            }

            if (element != null) {
                hit++;
                element.setAccessed();
                result.add(element);
            } else {
                miss++;
            }
        }

        return result;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public int getElementsCount() {
        return elements.size();
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    @Override
    public void removeElement(K key) {
        elements.remove(key);
    }

    private TimerTask getTimerTask(final K key, Function<Element<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                SoftReference<Element<K, V>> softReference = elements.get(key);
                if (softReference != null) {
                    Element<K, V> checkedElement = softReference.get();
                    if (checkedElement == null ||
                        isT1BeforeT2(timeFunction.apply(checkedElement), checkedElement.getCurrentTime())) {
                        elements.remove(key);
                    }
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
