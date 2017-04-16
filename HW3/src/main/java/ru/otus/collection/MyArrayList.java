package ru.otus.collection;

import java.util.*;

public class MyArrayList<T> extends AbstractList<T> implements List<T> {
    private T[] array;     //массив элементов
    private int size;      //под сколько элементов выделена память
    private int busySize;  //сколько элементов занято
    private float sizeFactor = 2.0f;  //во сколько раз увеличивается размер массива при расширении
                                      //(и уменьшается при сужении)

    //Constructors

    public MyArrayList() {
        this.array = (T[]) new Object[1];  //минимально - один элемент
        this.size = 1;
        this.busySize = 0;
    }

    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.array = (T[]) new Object[initialCapacity];
            this.size = initialCapacity;
            this.busySize = 0;
        }
        else if (initialCapacity == 0) {
            this.array = (T[]) new Object[1];
            this.size = initialCapacity;
            this.busySize = 0;
        }
        else
            throw new IllegalArgumentException("Illegal capacity: " +
                    initialCapacity);
    }

    public MyArrayList(Collection<? extends T> c) {
        this.array = (T[]) c.toArray();
        this.size = c.size();
        this.busySize = this.size;
    }

    public int size() {
        return busySize;
    }

    public boolean isEmpty() {
        return (busySize == 0);
    }

    public boolean contains(Object o) {
        if (o == null) {
           for (int i = 0; i < busySize; i++) {
               if (array[i] == null)
                   return true;
           }
        } else {
            for (int i = 0; i < busySize; i++) {
                if (array[i].equals(o))
                    return true;
            }
        }
        return false;
    }

    //поиск индекса элемента (с начала массива)
    private int containsWithIndex(Object o) {
        if (o == null) {
            for (int i = 0; i < busySize; i++) {
                if (array[i] == null)
                    return i;
            }
        } else {
            for (int i = 0; i < busySize; i++) {
                if (array[i].equals(o))
                    return i;
            }
        }
        return -1;
    }

    //поиск индекса элемента (с конца массива)
    private int containsLastIndex(Object o) {
        if (o == null) {
            for (int i = busySize-1; i >= 0; i--) {
                if (array[i] == null)
                    return i;
            }
        } else {
            for (int i = busySize-1; i >= 0; i--) {
                if (array[i].equals(o))
                    return i;
            }
        }
        return -1;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, busySize);
    }

    public <T1> T1[] toArray(T1[] a) {
        if (a.length < busySize)
            return (T1[]) Arrays.copyOf(array, busySize, a.getClass());
        System.arraycopy(array, 0, a, 0, busySize);
        if (a.length > busySize)
            a[busySize] = null;
        return a;
    }

    public boolean add(T t) {
        if (busySize + 1 <= size) {  //можно заполнить массив без расширения
            array[busySize++] = t;
        }
        else {  //иначе расширяем массив в 2 раза
            size = (int) (size * sizeFactor);
            if (size == 1)
                size = 2;  //на всякий случачй, т.к. если size = 1 и sizeFactor = 1.4, то массив бы не увеличился
            array = Arrays.copyOf(array, size);
            array[busySize++] = t;
        }
        return true;
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < busySize; i++) {
                if (array[i] == null) {
                    remove_i(i);
                    return true;
                }
            }
        }
        else {
            for (int i = 0; i < busySize; i++) {
                if (array[i].equals(o)) {
                    remove_i(i);
                    return true;
                }
            }
        }
        return false;
    }

    //удалить i-й элемент, сдвинуть элементы, чтобы не было "пустых" мест
    private void remove_i(int i) {
        if (i == (busySize-1)) {  //последний элемент
            //do nothing
        }
        else {  //не последний элемент
            for (int j = i; j < (busySize-1); j++) {
                array[j] = array[j+1];
            }
        }
        array[--busySize] = null;
        int sizeDivFactor = (int) (size / sizeFactor);
        if (sizeDivFactor == 0)
            sizeDivFactor = 1;  //на всякий случай, чтобы размер массива не уменьшился до нуля
        if (busySize < sizeDivFactor) {  //уменьшаем выделенный под массив размер
            size = sizeDivFactor;
            array = Arrays.copyOf(array, size);
        }
    }

    //Удалить из списка все вхождения заданного объекта
    private boolean removeAllInclusion(Object o) {
        int index = -1;
        boolean isListModify = false;
        while (this.contains(o)) {
            index = containsWithIndex(o);
            this.remove_i(index);
            isListModify = true;
        }
        return isListModify;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if (!this.contains(object))
                return false;
        }
        return true;
    }

    //Добавить все элементы в конец массива
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            this.add(t);
        }
        return true;
    }

    //Добавить все элементы в массив, начиная с заданной позиции (остальные сдвинуть вправо)
    public boolean addAll(int index, Collection<? extends T> c) {
        if ((index < 0) || (index >= busySize)) {
            throw new IndexOutOfBoundsException("index: " + index + " not correct");
        }
        int N_in = c.size();
        if (N_in == 0) {
            return false;
        }
        T[] array_in = (T[]) c.toArray();
        int newBusySize = busySize + N_in;
        while (size < newBusySize) {  //если элементов в массиве недостаточно для размещения новой коллекции
                                      //то увеличиваем массив
            size = (int) (size * sizeFactor);
        }

        array = Arrays.copyOf(array, size);

        for (int i = newBusySize-1; i >= index+N_in; i--) {  //сдвигаем элементы вправо
            array[i] = array[i-N_in];
        }
        for (int i = index; i < (index + N_in); i++) {
            array[i] = array_in[i - index];
        }

        busySize = newBusySize;
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        boolean isThisModify = false;
        for (Object t : c) {
            isThisModify = this.removeAllInclusion(t);
        }
        return isThisModify;
    }

    public boolean retainAll(Collection<?> c) {
        int index = 0;
        boolean isThisModify = false;
        while (index < busySize) {
            if (!c.contains(array[index])) {
                this.remove_i(index);
                isThisModify = true;
            } else {
                index++;
            }
        }
        return isThisModify;
    }

    public void sort(Comparator<? super T> c) {
        Arrays.sort(array, 0, busySize, c);
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        this.array = (T[]) new Object[1];
        this.size = 1;
        this.busySize = 0;
    }

    public T get(int index) {
        if ((index < 0) || (index >= busySize)) {
            throw new IndexOutOfBoundsException("index: " + index + " not correct");
        }
        return array[index];
    }

    public T set(int index, T element) {
        if ((index < 0) || (index >= busySize)) {
            throw new IndexOutOfBoundsException("index: " + index + " not correct");
        }
        T previousElement = array[index];
        array[index] = element;

        return previousElement;
    }

    public void add(int index, T element) {
        if ((index < 0) || (index >= busySize)) {
            throw new IndexOutOfBoundsException("index: " + index + " not correct");
        }

        int newBusySize = busySize + 1;
        while (size < newBusySize) {  //если элементов в массиве недостаточно для размещения новой коллекции
            //то увеличиваем массив
            size = (int) (size * sizeFactor);
        }

        array = Arrays.copyOf(array, size);

        for (int i = busySize; i > index ; i--) {  //сдвигаем элементы вправо
            array[i] = array[i-1];
        }
        array[index] = element;

        busySize = newBusySize;
    }

    public T remove(int index) {
        if ((index < 0) || (index >= busySize)) {
            throw new IndexOutOfBoundsException("index: " + index + " not correct");
        }
        T removedElement = array[index];
        remove_i(index);
        return removedElement;
    }

    public int indexOf(Object o) {
        int index = -1;
        if (this.contains(o)) {
            index = this.containsWithIndex(o);
        }
        return index;
    }

    public int lastIndexOf(Object o) {
        int index = -1;
        if (this.contains(o)) {
            index = this.containsLastIndex(o);
        }
        return index;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<T> {
        int cursor;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return (cursor != busySize);
        }

        @Override
        public T next() {
            int i = cursor;
            if (i >= busySize)
                throw new NoSuchElementException();
            T[] itArray = MyArrayList.this.array;
            cursor = i + 1;
            return itArray[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            MyArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    public ListIterator<T> listIterator() {
        return null;
    }

    public ListIterator<T> listIterator(int index) {
        return null;
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

}
