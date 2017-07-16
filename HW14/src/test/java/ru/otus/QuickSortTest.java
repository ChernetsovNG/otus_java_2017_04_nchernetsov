package ru.otus;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static ru.otus.ArrayUtils.arrayCompare;
import static ru.otus.ArrayUtils.arrayDisplay;
import static ru.otus.ArrayUtils.createRandomArray;

public class QuickSortTest {
    @Test
    public void sortTest() throws Exception {
        int maxSize = 16;

        long[] array = createRandomArray(maxSize);

        arrayDisplay(array);

        long[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);

        QuickSort quickSort = new QuickSort(array);
        quickSort.sort();

        arrayDisplay(array);

        Assert.assertTrue(arrayCompare(array, arrayCopy));
    }

    @Test
    public void parallelSortTest() throws Exception {
        int maxSize = 9;

        long[] array = createRandomArray(maxSize);

        arrayDisplay(array);

        long[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);

        ParallelQuickSort parallelQuickSort = new ParallelQuickSort(array, 4);
        parallelQuickSort.sort();

        arrayDisplay(array);

        //Assert.assertTrue(arrayCompare(array, arrayCopy));
    }

}