package ru.otus;

import ru.otus.memento.Memento;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class ATMDepartmentImpl implements ATMDepartment {
    private final Map<Integer, ATM> atmMap = new HashMap<>();
    private final Map<Integer, Memento> atmsMemento = new HashMap<>();

    // создать ATM с новым id
    private ATM getNextATM() {
        int maxId;
        if (atmMap.size() == 0) {
            maxId = -1;
        } else {
            maxId = atmMap.keySet().stream().max(Comparator.naturalOrder()).orElse(null);
        }
        return new ATM(maxId + 1);
    }

    @Override
    public void addATM() {
        ATM atm = getNextATM();
        atmMap.put(atm.getId(), atm);
        atmsMemento.put(atm.getId(), atm.getMemento());
    }

    @Override
    public void addATM(ATM atm) {
        atmMap.put(atm.getId(), atm);
        atmsMemento.put(atm.getId(), atm.getMemento());
    }

    @Override
    public void deleteATM(int id) {
        atmMap.remove(id);
    }

    @Override
    public ATM getATMbyID(int id) {
        return atmMap.get(id);
    }

    @Override
    public int atmCount() {
        return atmMap.size();
    }

    @Override
    public int getAllATMsRemainderSum() {
        int sum = atmMap.values().stream().mapToInt(ATM::getTotalAmount).sum();
        atmMap.values().forEach(ATM::withdrawTotalAmount);
        return sum;
    }

    @Override
    public void restoreATMsInitialState() {
        atmMap.values().forEach(
            atm -> {
                Memento memento = atmsMemento.get(atm.getId());
                atm.setMemento(memento);
            });
    }

}
