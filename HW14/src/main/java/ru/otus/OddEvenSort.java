package ru.otus;

// последовательная чётно-нечётная перестановка
class OddEvenSort extends ArraySort {

    OddEvenSort(long[] theArray) {
        this.theArray = theArray;
        nElems = theArray.length;
    }

    @Override
    void sort() {
        for (int i = 0; i < nElems; i++) {
            if (i % 2 == 1) {    // нечетная итерация
                for (int j = 0; j < nElems/2; j++) {
                    compareExchange(2*j, 2*j + 1);
                }
                if (nElems % 2 == 1) // сравнение последней пары при нечетном n
                    compareExchange(nElems - 2, nElems - 1);
            }
            else {    // четная итерация
                for (int j = 1; j < nElems/2; j++) {
                    compareExchange(2*j - 1, 2*j);
                }
            }
        }
    }
}
