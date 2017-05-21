package ru.otus;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.exception.NotEnoughMoneyException;

import java.util.Map;

import static ru.otus.strategy.WithdrawAlgorithmType.DYNAMIC_PROGRAMMING;

public class ATMTest {
    @Test
    public void Deposit3x500Test() {
        ATM atm = new ATM(1);
        atm.addAmount(500, 2);
        atm.addAmount(100, 5);
        Assert.assertEquals(1500, atm.getTotalAmount());
    }

    @Test
    public void hasMoneyTest() {
        ATM atm = new ATM(1);
        atm.addAmount(500, 1);
        Assert.assertTrue(atm.hasMoney());
    }

    @Test
    public void isAmountAvailableTest() {
        ATM atm = new ATM(1);
        atm.addAmount(500, 1);
        Assert.assertTrue(atm.isAmountAvailable(400));
        Assert.assertFalse(atm.isAmountAvailable(600));
    }

    @Test
    public void WithdrawGreedyTest() throws NotEnoughMoneyException {
        ATM atm = new ATM(1);
        atm.addAmount(500, 2);
        atm.addAmount(200, 3);
        Map<Integer, Integer> withdrawResult = atm.withdrawAmount(700);

        Assert.assertEquals((int) withdrawResult.get(500), 1);
        Assert.assertEquals((int) withdrawResult.get(200), 1);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void WithdrawGreedyNotAlwaysWorkTest() throws NotEnoughMoneyException {
        ATM atm = new ATM(1);
        atm.addAmount(500, 2);
        atm.addAmount(200, 3);
        atm.withdrawAmount(600);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void expectedAmountMoreThenTotalTest() throws NotEnoughMoneyException {
        ATM atm = new ATM(1);
        atm.addAmount(500, 2);
        atm.withdrawAmount(1200);
    }

    @Test
    public void DynamicProgrammingWithdrawAlgorithmTest() throws NotEnoughMoneyException {
        ATM atm = new ATM(1);
        atm.addAmount(500, 2);
        atm.addAmount(200, 3);
        atm.setWithdrawAlgorithm(DYNAMIC_PROGRAMMING);
        Map<Integer, Integer> withdrawResult = atm.withdrawAmount(600);

        Assert.assertEquals((int) withdrawResult.get(200), 3);
    }
}
