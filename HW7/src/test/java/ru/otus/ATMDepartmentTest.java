package ru.otus;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.exception.NotEnoughMoneyException;

public class ATMDepartmentTest {
    @Test
    public void ATMDepartmentAddSomeATMsTest() {
        ATMDepartment atmDepartment = new ATMDepartmentImpl();
        atmDepartment.addATM();
        atmDepartment.addATM();
        atmDepartment.addATM();
        Assert.assertEquals(3, atmDepartment.atmCount());
        Assert.assertEquals(2, atmDepartment.getATMbyID(2).getId());
    }

    @Test
    public void getTotalRemainderFromATMsTest() throws NotEnoughMoneyException {
        ATM atm1 = new ATM(1);
        ATM atm2 = new ATM(2);
        ATM atm3 = new ATM(3);

        atm1.addAmount(100, 10);
        atm2.addAmount(200, 3);
        atm3.addAmount(500, 3);

        ATMDepartment atmDepartment = new ATMDepartmentImpl();

        atmDepartment.addATM(atm1);
        atmDepartment.addATM(atm2);
        atmDepartment.addATM(atm3);

        atm1.withdrawAmount(500);

        Assert.assertEquals(2600, atmDepartment.getAllATMsRemainderSum());
        Assert.assertEquals(0, atm1.getTotalAmount());
        Assert.assertEquals(0, atm2.getTotalAmount());
        Assert.assertEquals(0, atm3.getTotalAmount());
    }

    @Test
    public void loadAllATMInitialStateTest() throws NotEnoughMoneyException {
        ATM atm1 = new ATM(1);
        ATM atm2 = new ATM(2);
        ATM atm3 = new ATM(3);

        atm1.addAmount(100, 10);
        atm2.addAmount(200, 3);
        atm3.addAmount(500, 3);

        ATMDepartment atmDepartment = new ATMDepartmentImpl();

        atmDepartment.addATM(atm1);
        atmDepartment.addATM(atm2);
        atmDepartment.addATM(atm3);

        atm1.withdrawAmount(500);
        atm2.withdrawAmount(400);
        atm3.withdrawAmount(1000);

        atmDepartment.restoreATMsInitialState();

        Assert.assertEquals(1000, atm1.getTotalAmount());
        Assert.assertEquals(600, atm2.getTotalAmount());
        Assert.assertEquals(1500, atm3.getTotalAmount());
    }
}
