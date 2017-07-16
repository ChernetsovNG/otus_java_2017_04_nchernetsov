package ru.otus;

class QuickSort {
    private int nElems;
    private long[] theArray;  // сортируемый массив

    QuickSort(long[] theArray) {
        nElems = theArray.length;
        this.theArray = theArray;
    }

    void sort() {
        recQuickSort(0, nElems - 1);
    }

    private void recQuickSort(int left, int right) {
        int size = right - left + 1;

        if (size <= 3)  {
            manualSort(left, right);  // ручная сортировка при малом размере
        } else {
            long median = medianOf3(left, right);

            int partition = partitionIt(left, right, median);  // разбиение диапазона

            recQuickSort(left, partition - 1);    // сортировка левой части
            recQuickSort(partition + 1, right);    // сортировка правой части
        }
    }

    // метод возвращает медиану по первому, среднему и последнему элементам массива,
    // а также упорядочивает эти элементы по возрастанию
    private long medianOf3(int left, int right) {
        int center = (left + right)/2;

        if (theArray[left] > theArray[center]) {  // упорядочение left и center
            swap(left, center);
        }

        if (theArray[left] > theArray[right]) {  // упорядочение left и right
            swap(left, right);
        }

        if (theArray[center] > theArray[right]) {  // упорядочение center и right
            swap(center, right);
        }

        swap(center, right - 1);  // размещение медианы на правом крае
        return theArray[right - 1];
    }

    // метод разбиения массива на два подмассива со значениями, большими и меньшими опорного элемента
    // возвращает крайний левый элемент правого (большего) подмассива
    private int partitionIt(int left, int right, long pivot) {
        int leftPtr = left;        // справа от первого элемента
        int rightPrt = right - 1;  // слева от опорного элемента

        while (true) {
            while (theArray[++leftPtr] < pivot) {  // поиск большего элемента
                // do nothing
            }

            while (theArray[--rightPrt] > pivot) {  // поиск меньшего элемента
                // do nothing
            }

            if (leftPtr >= rightPrt) {    // если указатели сошлись,
                break;                    // то разбиение закончено.
            } else {                      // в противном случае
                swap(leftPtr, rightPrt);  // поменять элементы местами
            }
        }

        swap(leftPtr, right - 1);  // восстановление опорного элемента
        return leftPtr;                 // позиция разбиения
    }

    private void manualSort(int left, int right) {
        int size = right - left + 1;

        if (size <= 1) {
            return;  // сортировка не требуется
        } else if (size == 2) {
            if (theArray[left] > theArray[right]) {
                swap(left, right);
                return;
            }
        } else {  // размер равен 3
            if (theArray[left] > theArray[right - 1]) {
                swap(left, right - 1);  // left, center
            }
            if (theArray[left] > theArray[right]) {
                swap(left, right);           // left, right
            }
            if (theArray[right - 1] > theArray[right]) {
                swap(right - 1, right);  // center, right
            }
        }


    }

    // перестановка двух элементов
    private void swap(int dex1, int dex2) {
        long temp = theArray[dex1];
        theArray[dex1] = theArray[dex2];
        theArray[dex2] = temp;
    }
}
