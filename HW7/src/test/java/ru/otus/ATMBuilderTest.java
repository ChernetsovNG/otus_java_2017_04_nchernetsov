package ru.otus;

import org.junit.Test;
import ru.otus.builder.ATMBuilder;
import ru.otus.builder.ATMBuilderImpl;

import static org.junit.Assert.assertEquals;

public class ATMBuilderTest {

    @Test
    public void atmBuiderTest() {
        ATMBuilder atmBuilder = new ATMBuilderImpl(1);

        ATM atm = atmBuilder
            .addAmount(50, 10)
            .addAmount(100, 5)
            .addAmount(500, 2)
            .build();

        assertEquals(1, atm.getId());
        assertEquals(2000, atm.getTotalAmount());
    }
}
