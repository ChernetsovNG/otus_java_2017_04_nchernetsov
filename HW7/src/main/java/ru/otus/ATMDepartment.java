package ru.otus;

public interface ATMDepartment {
    void addATM();

    void addATM(ATM atm);

    void deleteATM(int id);

    ATM getATMbyID(int id);

    /**
     * Списать сумму остатков со всех ATM
     *
     * @return - сумма остатков
     */
    int getAllATMsRemainderSum();

    /**
     * Восстановить начальное состояние всех ATM
     */
    void restoreATMsInitialState();

    int atmCount();
}
