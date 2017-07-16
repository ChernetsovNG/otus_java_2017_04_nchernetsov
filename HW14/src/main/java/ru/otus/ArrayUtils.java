package ru.otus;

class ArrayUtils {
    static void arrayDisplay(int[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]).append(" ");
        }
        sb.append(array[array.length - 1]);

        System.out.println(sb.toString());
    }

    static void arrayDisplay(long[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]).append(" ");
        }
        sb.append(array[array.length - 1]);

        System.out.println(sb.toString());
    }

    static boolean arrayCompare(long[] array1, long[] array2) {
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

    static long[] createRandomArray(int size) {
        long[] array = new long[size];

        for (int i = 0; i < size; i++) {
            long n = (int) (Math.random() * 99);
            array[i] = n;
        }

        return array;
    }
}
