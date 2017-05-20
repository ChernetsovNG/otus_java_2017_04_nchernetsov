package ru.otus;

import ru.otus.exception.NotEnoughMoneyException;
import ru.otus.strategy.WithdrawAlgorithm;
import ru.otus.strategy.WithdrawAlgorithmType;

import java.util.*;

public class ATM implements WithdrawAlgorithm {
    private int id;
    private Map<Integer, Integer> denominations = new HashMap<>();
    private WithdrawAlgorithm withdrawAlgorithm;

    ATM(int id) {
        this.id = id;
        this.withdrawAlgorithm = new GreedyWithdrawAlgorithm();  //по умолчанию - жадный алгоритм
    }

    void setWithdrawAlgorithm(WithdrawAlgorithmType algorithmType) {
        switch (algorithmType) {
            case GREEDY:
                this.withdrawAlgorithm = new GreedyWithdrawAlgorithm();
                break;
            case DYNAMIC_PROGRAMMING:
                this.withdrawAlgorithm = new DynamicProgrammingWithdrawAlgorithm();
                break;
        }
    }

    void addAmount(int nominal, int count) {
        if (denominations.containsKey(nominal)) {
            denominations.put(nominal, denominations.get(nominal) + count);
        } else {
            denominations.put(nominal, count);
        }
    }

    @Override
    public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
        return withdrawAlgorithm.withdrawAmount(expectedAmount);
    }

    int getTotalAmount() {
        int totalAmount = 0;
        for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
            int denomination = entry.getKey();
            int count = entry.getValue();
            totalAmount += denomination * count;
        }
        return totalAmount;
    }

    Map<Integer, Integer> withdrawTotalAmount() {
        Map<Integer, Integer> totalWithdraw = denominations;
        denominations.clear();
        return totalWithdraw;
    }

    boolean hasMoney() {
        return (denominations.keySet().size() > 0);
    }

    boolean isAmountAvailable(int expectedAmount) {
        return (getTotalAmount() >= expectedAmount);
    }

    int getId() { return id; }

    //Алгоритмы списания денег

    //"Жадный" алгоритм
    private class GreedyWithdrawAlgorithm implements WithdrawAlgorithm {
        @Override
        public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
            int amount = expectedAmount;

            HashMap<Integer, Integer> denomCopy = new HashMap<>();
            denomCopy.putAll(denominations);

            List<Integer> nominalListByDesc = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : denomCopy.entrySet()) {
                nominalListByDesc.add(entry.getKey());
            }
            Collections.sort(nominalListByDesc);
            Collections.reverse(nominalListByDesc);  //список номиналов купюр по убыванию

            TreeMap<Integer, Integer> withdrawResult = new TreeMap<>(Comparator.reverseOrder());

            for (Integer nominal : nominalListByDesc) {
                int count = denomCopy.get(nominal);  //количество купюр максимального достоинства
                while (true) {
                    if (amount < nominal || count <= 0) {
                        denomCopy.put(nominal, count);
                        break;
                    }
                    amount -= nominal;  //вычитаем одну купюру максимального достоинства
                    count--;

                    if (withdrawResult.containsKey(nominal))
                        withdrawResult.put(nominal, withdrawResult.get(nominal) + 1);
                    else
                        withdrawResult.put(nominal, 1);
                }
            }

            if (amount > 0)
                throw new NotEnoughMoneyException();
            else {
                denominations.clear();
                denominations.putAll(denomCopy);  //обновляем хранимые в APM купюры
                return withdrawResult;
            }
        }
    }

    //Алгоритм динамического программирования
    private class DynamicProgrammingWithdrawAlgorithm implements WithdrawAlgorithm {
        @Override
        public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
            Map<Integer, Integer> denomByNominalAscend = new TreeMap<>();

            denomByNominalAscend.putAll(denominations);

            Map<Integer, Integer> result = new TreeMap<>(Collections.reverseOrder());

            long INF = Long.MAX_VALUE - 1;

            //Массив minBanknoteCount[currSum] содержит минимальное количество банкнот, которыми можно выдать сумму currSum,
            //либо +INF, если эту сумму выдать невозможно
            long minBanknoteCount[] = new long[expectedAmount + 1];
            minBanknoteCount[0] = 0;

            for (int currSum = 1; currSum <= expectedAmount; currSum++) {
                minBanknoteCount[currSum] = INF;

                for (Map.Entry<Integer, Integer> entry : denomByNominalAscend.entrySet()) {
                    int denom = entry.getKey();
                    int count = entry.getValue();

                    if ((currSum >= denom) && (count > 0)) {
                        if (minBanknoteCount[currSum - denom] + 1 < minBanknoteCount[currSum]) {
                            minBanknoteCount[currSum] = minBanknoteCount[currSum - denom] + 1;
                            denomByNominalAscend.put(denom, denomByNominalAscend.get(denom) - 1);
                        }
                    }
                }
            }

            if (minBanknoteCount[expectedAmount] == INF) {
                throw new NotEnoughMoneyException();
            } else {
                int n = expectedAmount;
                while (n > 0) {
                    for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
                        int denom = entry.getKey();
                        int count = entry.getValue();

                        if ((count > 0) && (n >= denom)) {
                            if (minBanknoteCount[n - denom] == minBanknoteCount[n] - 1) {
                                if (result.containsKey(denom)) {
                                    result.put(denom, result.get(denom) + 1);
                                } else {
                                    result.put(denom, 1);
                                }
                                denominations.put(denom, denominations.get(denom) - 1);
                                n -= denom;
                                break;
                            }
                        }
                    }
                }
                return result;
            }
        }
    }

}
