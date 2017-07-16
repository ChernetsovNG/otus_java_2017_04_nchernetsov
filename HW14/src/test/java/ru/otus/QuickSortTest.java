package ru.otus;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class QuickSortTest {

    private void arrayDisplay(long[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]).append(" ");
        }
        sb.append(array[array.length - 1]);

        System.out.println(sb.toString());
    }

    private boolean arrayCompare(long[] array1, long[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void sortTest() throws Exception {
        int maxSize = 16;

        long[] array = new long[maxSize];

        for (int i = 0; i < maxSize; i++) {
            long n = (int) (Math.random() * 99);
            array[i] = n;
        }

        arrayDisplay(array);

        long[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);

        QuickSort quickSort = new QuickSort(array);
        quickSort.sort();

        arrayDisplay(array);

        Assert.assertTrue(arrayCompare(array, arrayCopy));
    }

}