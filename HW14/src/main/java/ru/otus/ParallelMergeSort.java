package ru.otus;

import java.util.Arrays;

class ParallelMergeSort {
    static void sort(long[] array, int nThreads) {
        sort(array, 0, array.length, nThreads);
    }

    private static void sort(long[] array, int fromIndex, int toIndex, int nThreads) {
        int rangeLength = toIndex - fromIndex;

        int threads = numThreadsAsPowerOfTwo(nThreads);

        if (threads < 2) {
            BottomUpMergeSort.sort(array, fromIndex, toIndex);
            return;
        }

        // разбиваем массив на левую и правую часть
        int leftPartLength  = rangeLength/2;
        int rightPartLength = rangeLength - leftPartLength;
        // копия входного массива
        long[] aux = Arrays.copyOfRange(array, fromIndex, toIndex);

        // делим потоки пополам
        int nThreads1 = threads/2;
        int nThreads2 = threads - nThreads1;

        // сортируем левую половину массива
        SorterThread thread1 = new SorterThread(nThreads1, array, aux, fromIndex, 0, leftPartLength);

        thread1.start();

        // сортируем правую половину массива
        SorterThread thread2 = new SorterThread(nThreads2, array, aux,
            fromIndex + leftPartLength, leftPartLength, rightPartLength);
        thread2.run();

        try {
            thread1.join();
        } catch (InterruptedException ex) {
            throw new IllegalStateException(
                "A SorterThread threw an IllegalStateException.");
        }

        // сливаем отсортированные массивы
        merge(aux, array, 0, fromIndex, leftPartLength, rightPartLength);
    }

    private static void merge(long[] source, long[] target, int sourceOffset,
                              int targetOffset, int leftRunLength, int rightRunLength) {
        int left = sourceOffset;
        int leftUpperBound = sourceOffset + leftRunLength;
        int right = leftUpperBound;
        int rightUpperBound = leftUpperBound + rightRunLength;
        int targetIndex = targetOffset;

        while (left < leftUpperBound && right < rightUpperBound) {
            target[targetIndex++] = source[right] < source[left] ? source[right++] : source[left++];
        }

        System.arraycopy(source, left, target, targetIndex, leftUpperBound - left);
        System.arraycopy(source, right, target, targetIndex, rightUpperBound - right);
    }

    // округляем заданное число потоков до ближайшей большей степени двойки, т.е. 1,2,4,8,16,... потоков
    private static int numThreadsAsPowerOfTwo(int threads) {
        int ret = 1;

        while (ret < threads) {
            ret <<= 1;
        }

        return ret;
    }

    private static final class SorterThread extends Thread {
        private final int threads;  // количество потоков
        private final long[] source;
        private final long[] target;
        private final int sourceOffset;
        private final int targetOffset;
        private final int rangeLength;

        SorterThread(int threads, long[] source, long[] target,
                     int sourceOffset, int targetOffset, int rangeLength) {
            this.threads = threads;
            this.source = source;
            this.target = target;
            this.sourceOffset = sourceOffset;
            this.targetOffset = targetOffset;
            this.rangeLength = rangeLength;
        }

        @Override
        public void run() {
            if (threads < 2) {
                BottomUpMergeSort.sort(target, targetOffset, targetOffset + rangeLength);
                return;
            }

            int leftPartLength = rangeLength/2;

            SorterThread thread1 = new SorterThread(threads/2, target, source,
                targetOffset, sourceOffset, leftPartLength);

            thread1.start();

            SorterThread thread2 = new SorterThread(threads - threads/2, target, source,
                targetOffset + leftPartLength,
                sourceOffset + leftPartLength,
                rangeLength - leftPartLength);

            thread2.run();

            try {
                thread1.join();
            } catch (InterruptedException ex) {
                throw new IllegalStateException(
                    "A SorterThread threw InterruptedException.");
            }

            merge(source, target, sourceOffset, targetOffset, leftPartLength, rangeLength - leftPartLength);
        }
    }
}
