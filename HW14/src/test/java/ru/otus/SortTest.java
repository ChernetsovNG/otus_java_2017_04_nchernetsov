package ru.otus;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static ru.otus.ArrayUtils.*;

public class SortTest {
    @Test
    public void sortTest() {
        int size = 16;

        long[] array = createRandomArray(size, 100);

        arrayDisplay(array);

        long[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);

        QuickSort quickSort = new QuickSort(array);
        quickSort.sort();

        arrayDisplay(array);

        Assert.assertTrue(arrayCompare(array, arrayCopy));
    }

    @Test
    public void oddEvenSortTest() {
        int size = 8;

        long[] array = createRandomArray(size, 100);

        arrayDisplay(array);

        long[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);

        OddEvenSort oddEvenSort = new OddEvenSort(array);
        oddEvenSort.sort();

        arrayDisplay(array);

        Assert.assertTrue(arrayCompare(array, arrayCopy));
    }

    @Test
    public void bottomUpMergeSortTest() {
        int size = 8;

        long[] array = createRandomArray(size, 100);

        arrayDisplay(array);

        long[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);

        BottomUpMergeSort.sort(array);

        arrayDisplay(array);

        Assert.assertTrue(arrayCompare(array, arrayCopy));
    }

    @Test
    public void parallelMergeSortTest() {
        int size = 16;

        long[] array = createRandomArray(size, 100);

        arrayDisplay(array);

        long[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);

        ParallelMergeSort.sort(array, 4);

        arrayDisplay(array);

        Assert.assertTrue(arrayCompare(array, arrayCopy));
    }

    @Test
    public void tmpTest() {
        System.out.println(Integer.numberOfLeadingZeros(9));
    }

}