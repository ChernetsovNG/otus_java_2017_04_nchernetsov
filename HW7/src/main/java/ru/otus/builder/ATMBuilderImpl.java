package ru.otus.builder;

import ru.otus.ATM;

public class ATMBuilderImpl implements ATMBuilder {

    private final ATM atm;

    public ATMBuilderImpl(int atmId) {
        atm = new ATM(atmId);
    }

    @Override
    public ATMBuilder addAmount(int nominal, int count) {
        atm.addAmount(nominal, count);
        return this;
    }

    @Override
    public ATM build() {
        return atm;
    }
}
