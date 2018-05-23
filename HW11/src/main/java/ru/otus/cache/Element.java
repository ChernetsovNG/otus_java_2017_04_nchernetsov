package ru.otus.cache;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Element<K, V> {
    private final K key;
    private final V value;
    /**
     * Время создания элемента, UTC, мс
     */
    private final long creationTime;
    /**
     * Время последнего доступа к элементу, UTC, мс
     */
    private long lastAccessTime;

    public Element(K key, V value) {
        this.key = key;
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    long getCurrentTime() {
        return LocalDateTime.now(Clock.systemUTC()).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    long getCreationTime() {
        return creationTime;
    }

    long getLastAccessTime() {
        return lastAccessTime;
    }

    void setAccessed() {
        lastAccessTime = getCurrentTime();
    }
}
