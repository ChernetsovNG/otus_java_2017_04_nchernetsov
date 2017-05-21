package ru.otus.memento;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Memento {
    private final Map<Integer, Integer> denominations;

    public Memento(Map<Integer, Integer> denominations) {
        this.denominations = Collections.unmodifiableMap(new HashMap<>(denominations));
    }

    public Map<Integer, Integer> getState() {
        return denominations;
    }
}
