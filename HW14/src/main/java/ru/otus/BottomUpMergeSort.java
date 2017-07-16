package ru.otus;

import java.util.Arrays;

class BottomUpMergeSort {

    private static final int INSERTIONSORT_THRESHOLD = 4;  // порог, после которого применяем сортировку вставками

    private final long[] array;   // Массив, содержащий элементы для сортировки
    private final long[] buffer;  // Вспомогательный массив для слияния

    private final int fromIndex;    // Начальный индекс диапазона для сортировки
    private final int rangeLength;  // Длина сортируемого диапазона

    private long[] source;  // Входной массив
    private long[] target;  // Целевой массив

    private int sourceOffset;  // Количество пропускаемых элементов от начала во входном массиве
    private int targetOffset;  // Количество пропускаемых элементов от начала в целевом массиве

    static void sort(long[] array) {
        sort(array, 0, array.length);
    }

    static void sort(long[] array, int fromIndex, int toIndex) {
        new BottomUpMergeSort(array, fromIndex, toIndex).sort();
    }

    private BottomUpMergeSort(long[] array, int fromIndex, int toIndex) {
        this.fromIndex = fromIndex;
        this.array = array;
        this.rangeLength = toIndex - fromIndex;
        this.buffer = Arrays.copyOfRange(array, fromIndex, toIndex);
    }

    // Метод сортировки
    private void sort() {
        if (rangeLength < 2) {  // 1 элемент - уже отсортирован
            return;
        }

        int insertionSortBlocksAmount = computeInsertionSortBlocksAmount();  // количество блоков, которые сортируются вставками
        int mergePasses = computeAmountOfMergingPasses(insertionSortBlocksAmount);

        if (mergePasses % 2 == 0) {
            this.source = array;
            this.target = buffer;
            this.sourceOffset = fromIndex;
            this.targetOffset = 0;
        } else {
            this.source = buffer;
            this.target = array;
            this.sourceOffset = 0;
            this.targetOffset = fromIndex;
        }

        // Сортируем входной массив на отсортированные блоки, каждый длиной insertionsortThreshold
        // Последний блок может быть короче
        presortRuns(insertionSortBlocksAmount);

        // Отсортированные вставками блоки готовы для слияния
        for (int runLength = INSERTIONSORT_THRESHOLD; runLength < rangeLength; runLength *= 2) {
            mergePass(insertionSortBlocksAmount, runLength);
            insertionSortBlocksAmount = (insertionSortBlocksAmount/2) + ((insertionSortBlocksAmount & 1) != 0 ? 1 : 0);
            // Делаем входной массив целевым и наоборот
            swapArrayRoles();
        }
    }

    private void swapArrayRoles() {
        // Swap the array roles
        long[] tmparr = source;
        source = target;
        target = tmparr;

        // Swap the array offsets
        int tmpOffset = sourceOffset;
        sourceOffset = targetOffset;
        targetOffset = tmpOffset;
    }

    // Вычисляет количество прогонов в запрошенном диапазоне, которые должны сортироваться с помощью сортировки вставками
    private int computeInsertionSortBlocksAmount() {
        return (rangeLength/INSERTIONSORT_THRESHOLD) + (rangeLength % INSERTIONSORT_THRESHOLD != 0 ? 1 : 0);
    }

    // Computes the amount of merging passes needed to be performed in order to
    // sort the requested range
    private int computeAmountOfMergingPasses(int blocks) {
        return 32 - Integer.numberOfLeadingZeros(blocks - 1);
    }

    private void presortRuns(int runs) {
        int localFromIndex = sourceOffset;

        // Сортируем вставками все блоки входного массива, кроме последнего
        for (int i = 0; i < runs - 1; ++i) {
            insertionSort(source, localFromIndex, localFromIndex += INSERTIONSORT_THRESHOLD);
        }

        // Сортируем последний блок (он может быть короче insertionsortThreshold)
        insertionSort(source, localFromIndex, Math.min(sourceOffset + rangeLength, localFromIndex + INSERTIONSORT_THRESHOLD));
    }

    private void mergePass(int sortedBlocksAmount, int runLength) {
        int runIndex = 0;

        for (; runIndex < sortedBlocksAmount - 1; runIndex += 2) {
            // Set up the indices.
            int leftIndex = sourceOffset + runIndex*runLength;
            int leftBound = leftIndex + runLength;
            int rightBound = Math.min(leftBound + runLength, rangeLength + sourceOffset);
            int targetIndex = targetOffset + runIndex*runLength;

            // Выполняем слияние
            merge(leftIndex, leftBound, rightBound, targetIndex);
        }

        if (runIndex < sortedBlocksAmount) {
            System.arraycopy(source, sourceOffset + runIndex*runLength, target,
                targetOffset + runIndex*runLength, rangeLength - runIndex*runLength);
        }
    }

    private static void insertionSort(long[] array, int fromIndex, int toIndex) {
        for (int i = fromIndex + 1; i < toIndex; ++i) {
            long element = array[i];
            int j = i;

            for (; j > fromIndex && array[j - 1] > element; --j) {
                array[j] = array[j - 1];
            }

            array[j] = element;
        }
    }

    private void merge(int leftIndex, int leftBound, int rightBound, int targetIndex) {
        int rightIndex = leftBound;

        while (leftIndex < leftBound && rightIndex < rightBound) {
            target[targetIndex++] = source[rightIndex] < source[leftIndex] ? source[rightIndex++] : source[leftIndex++];
        }

        System.arraycopy(source, leftIndex, target, targetIndex, leftBound - leftIndex);
        System.arraycopy(source, rightIndex, target, targetIndex, rightBound - rightIndex);
    }
}
