package ru.otus.memento;

public interface Originator {
    Memento getMemento();
    void setMemento(Memento memento);
}
