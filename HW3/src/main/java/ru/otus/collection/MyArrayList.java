package ru.otus.collection;

import java.util.*;

import static java.lang.Math.sqrt;

public class MyArrayList<T> extends AbstractList<T> implements List<T> {
    private static final Object[] EMPTY_ARRAY = {};

    private T[] array;     //массив элементов
    private int size;      //под сколько элементов выделена память
    private int busySize;  //сколько элементов занято
    private float sizeFactor = (float) ((1+sqrt(5))/2.0f);
    //во сколько раз увеличивается размер массива при расширении
    //(и уменьшается при сужении). Берём в качестве множителя золотое сечение

    //Constructors
    public MyArrayList() {
        this.array = (T[]) EMPTY_ARRAY;
        this.size = 0;
        this.busySize = 0;
    }

    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.array = (T[]) new Object[initialCapacity];
            this.size = initialCapacity;
            this.busySize = 0;
        }
        else if (initialCapacity == 0) {
            this.array = (T[]) EMPTY_ARRAY;
            this.size = 0;
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
        for (int i = 0; i < busySize; i++) {
            if (Objects.equals(array[i], o))
                return true;
        }
        return false;
    }

    //поиск индекса элемента (с начала массива)
    private int containsWithIndex(Object o) {
        for (int i = 0; i < busySize; i++) {
            if (Objects.equals(array[i], o)) {
                return i;
            }
        }
        return -1;
    }

    //поиск индекса элемента (с конца массива)
    private int containsLastIndex(Object o) {
        for (int i = busySize-1; i >= 0; i--) {
            if (Objects.equals(array[i], o))
                return i;
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
            //do nothing
        }
        else {  // используем мультипликативную схему перевыделения (реаллокации) памяти
            if (size > Integer.MAX_VALUE/sizeFactor)
                size = Integer.MAX_VALUE;
            else {
                if (size == 0) size = 1;
                else {
                    int newSize = (int) (size * sizeFactor);
                    if (newSize == size)  //т.е. sizeFactor задан такой, что size не увеличивается
                        size += 1;        //(например size = 2 и sizeFactor = 1.2)
                    else
                        size = newSize;
                }
            }
            array = Arrays.copyOf(array, size);
        }
        try {
            if (busySize + 1 > size) {
                throw new IllegalStateException("Array is full and cannot be extended more");
            }
            array[busySize++] = t;
            return true;
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < busySize; i++) {
                if (array[i] == null) {
                    removeAtIndex(i);
                    return true;
                }
            }
        }
        else {
            for (int i = 0; i < busySize; i++) {
                if (array[i].equals(o)) {
                    removeAtIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    //удалить i-й элемент, сдвинуть элементы, чтобы не было "пустых" мест
    private void removeAtIndex(int i) {
        if (i == (busySize-1)) {  //последний элемент
            //do nothing
        }
        else {  //не последний элемент
            for (int j = i; j < (busySize-1); j++) {
                array[j] = array[j+1];
            }
        }
        array[--busySize] = null;
    }

    //Удалить из списка все вхождения заданного объекта
    private boolean removeAllInclusion(Object o) {
        int index = -1;
        boolean isListModify = false;
        while (this.contains(o)) {
            index = containsWithIndex(o);
            this.removeAtIndex(index);
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
                this.removeAtIndex(index);
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
        this.array = (T[]) EMPTY_ARRAY;
        this.size = 0;
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
        removeAtIndex(index);
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
