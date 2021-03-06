package ru.otus.cache;

import java.util.List;

public interface CacheEngine<K, V> {
    void put(Element<K, V> element);

    Element<K, V> get(K key);

    void removeElement(K key);

    List<Element<K, V>> getAll();

    int getHitCount();

    int getMissCount();

    int getElementsCount();

    void dispose();
}
