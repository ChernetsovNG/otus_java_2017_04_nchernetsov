package ru.otus.builder;

import ru.otus.ATM;

import java.util.HashMap;
import java.util.Map;

public class ATMBuilderImpl implements ATMBuilder {

    private final int atmId;
    private final Map<Integer, Integer> denominations;

    public ATMBuilderImpl(int atmId) {
        this.atmId = atmId;
        this.denominations = new HashMap<>();
    }

    @Override
    public ATMBuilder addAmount(int nominal, int count) {
        if (denominations.containsKey(nominal)) {
            denominations.put(nominal, denominations.get(nominal) + count);
        } else {
            denominations.put(nominal, count);
        }
        return this;
    }

    @Override
    public ATM build() {
        return new ATM(atmId, denominations);
    }
}
