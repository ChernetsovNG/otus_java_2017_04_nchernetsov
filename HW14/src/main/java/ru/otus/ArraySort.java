package ru.otus;

// сортировка элементов массива по возрастанию
abstract class ArraySort {
    int nElems;
    long[] theArray;  // сортируемый массив

    abstract void sort();

    // сравнить и переставить
    void compareExchange(int index1, int index2) {
        if (theArray[index1] > theArray[index2]) {
            swap(index1, index2);
        }
    }

    // перестановка двух элементов
    void swap(int index1, int index2) {
        long temp = theArray[index1];
        theArray[index1] = theArray[index2];
        theArray[index2] = temp;
    }
}
