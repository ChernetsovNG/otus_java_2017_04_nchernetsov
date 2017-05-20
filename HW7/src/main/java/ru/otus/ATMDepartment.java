package ru.otus;

import java.util.*;

public class ATMDepartment {
    private Map<Integer, ATM> atmMap = new HashMap<>();

    //создать ATM с новым id
    private ATM getNextATM() {
        int maxId;
        if (atmMap.size() == 0) {
            maxId = -1;
        } else {
            maxId = atmMap.keySet().stream().max(Comparator.naturalOrder()).get();
        }
        return new ATM(maxId + 1);
    }

    void addATM() {
        ATM atm = getNextATM();
        atmMap.put(atm.getId(), atm);
    }

    void addATM(ATM atm) {
        atmMap.put(atm.getId(), atm);
    }

    public void deleteATM(int id) {
        atmMap.remove(id);
    }

    ATM getATMbyID(int id) {
        return atmMap.get(id);
    }

    int size() {
        return atmMap.size();
    }

    //Списать сумму остатков
    int getAllATMsRemainderSum() {
        int sum = 0;
        for (Map.Entry<Integer, ATM> entry : atmMap.entrySet()) {
            ATM atm = entry.getValue();
            sum += atm.getTotalAmount();
            atm.withdrawTotalAmount();
        }
        return sum;
    }

}
