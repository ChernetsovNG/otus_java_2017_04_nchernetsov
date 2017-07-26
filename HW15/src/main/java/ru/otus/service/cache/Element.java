package ru.otus.service.cache;

public class Element<K, V> {
    private final K key;
    private final V value;
    private final long creationTime;
    private long lastAccessTime;

    public Element(K key, V value) {
        this.key = key;
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    long getCurrentTime() {
        return System.currentTimeMillis();
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
