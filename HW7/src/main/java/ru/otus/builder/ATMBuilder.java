package ru.otus.builder;

import ru.otus.ATM;

public interface ATMBuilder {
    ATMBuilder addAmount(int nominal, int count);

    ATM build();
}
