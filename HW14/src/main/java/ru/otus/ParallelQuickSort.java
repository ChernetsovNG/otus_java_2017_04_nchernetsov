package ru.otus;

import static ru.otus.ArrayUtils.arrayDisplay;

class ParallelQuickSort {
    private int nElems;

    private long[] theArray;  // сортируемый массив
    private int nThreads;     // количество потоков

    private int[] startIndexes;  // индексы обрабатываемых частей массива для каждого потока
    private int[] endIndexes;

    ParallelQuickSort(long[] theArray, int nThreads) {
        nElems = theArray.length;
        this.theArray = theArray;
        this.nThreads = nThreads;

        startIndexes = new int[nThreads];
        endIndexes = new int[nThreads];
    }

    void sort() {
        // делим массив на части
        int partSize = nElems / nThreads;

        // находим индексы частей массива, которые должен сортировать каждый поток
        for (int i = 0; i < nThreads - 1; i++) {
            startIndexes[i] = i*partSize;
            endIndexes[i] = startIndexes[i] + partSize - 1;
        }
        startIndexes[nThreads - 1] = endIndexes[nThreads - 2] + 1;
        endIndexes[nThreads - 1] = nElems - 1;
    }
}
