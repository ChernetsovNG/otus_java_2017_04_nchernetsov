package ru.otus.collection;

import java.util.*;

public class MyArrayList<T> implements List<T> {
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

    @Override
    public Iterator<T> iterator() {
        return null;
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
        if (busySize < sizeDivFactor) {  //уменьшаем выделенный под массив размер
            size = sizeDivFactor;
            array = Arrays.copyOf(array, size);
        }
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
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void sort(Comparator<? super T> c) {

    }

    public void clear() {

    }

    public T get(int index) {
        return null;
    }

    public T set(int index, T element) {
        return null;
    }

    public void add(int index, T element) {

    }

    public T remove(int index) {
        return null;
    }

    public int indexOf(Object o) {
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
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
