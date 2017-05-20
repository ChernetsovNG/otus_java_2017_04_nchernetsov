package ru.otus.strategy;

import ru.otus.ATM;
import ru.otus.exception.NotEnoughMoneyException;

import java.util.Map;

public interface WithdrawAlgorithm {
    //Возвращает карту <номинал, количество купюр>, которыми можно выдать требуемую сумму
    //(либо кидает исключение, если сумму выдать невозможно)
    Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException;
}
