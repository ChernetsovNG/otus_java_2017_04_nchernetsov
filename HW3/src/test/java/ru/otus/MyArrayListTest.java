package ru.otus;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

public class MyArrayListTest {

  @Test
  public void constructorWithoutParametersTest() {
    List<Integer> list = new MyArrayList<>();
    assertEquals(0, list.size());
  }

  @Test
  public void constructorWithInitialCapacityTest() {
    List<Integer> list = new MyArrayList<>(5);
    assertEquals(0, list.size());
  }

  @Test
  public void constructorFromOtherListTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    assertEquals(5, list.size());
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
    assertEquals(4, (int) list.get(3));
    assertEquals(5, (int) list.get(4));
  }

  @Test
  public void isEmptyTest() {
    List<Integer> list1 = new MyArrayList<>();
    assertTrue(list1.isEmpty());
    List<Integer> list2 = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    assertFalse(list2.isEmpty());
  }

  @Test
  public void containsTest() {
    List<Integer> list1 = new MyArrayList<>();
    assertFalse(list1.contains(3));
    List<Integer> list2 = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    assertTrue(list2.contains(3));
  }

  @Test
  public void toArrayTest() {
    List<Integer> list1 = new MyArrayList<>();
    Object[] array1 = list1.toArray();
    assertEquals(0, array1.length);
    assertArrayEquals(new Integer[]{}, array1);

    List<Integer> list2 = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    Object[] array2 = list2.toArray();
    assertEquals(5, array2.length);
    assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, array2);
  }

  @Test
  public void addTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    list.add(10);
    list.add(15);
    list.add(20);

    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
    assertEquals(4, (int) list.get(3));
    assertEquals(5, (int) list.get(4));
    assertEquals(10, (int) list.get(5));
    assertEquals(15, (int) list.get(6));
    assertEquals(20, (int) list.get(7));
  }

  @Test
  public void removeTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    assertFalse(list.remove(new Integer(12)));
    assertTrue(list.remove(new Integer(3)));
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(4, (int) list.get(2));
    assertEquals(5, (int) list.get(3));
  }

  @Test
  public void containsAllTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    assertFalse(list.containsAll(Arrays.asList(1, 2, 15)));
    assertTrue(list.containsAll(Arrays.asList(1, 2, 4, 5)));
  }

  @Test
  public void addAllTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    list.addAll(Arrays.asList(4, 5));
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
    assertEquals(4, (int) list.get(3));
    assertEquals(5, (int) list.get(4));
  }

  @Test
  public void addAllIntoIndexTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    list.addAll(1, Arrays.asList(4, 5));
    assertEquals(1, (int) list.get(0));
    assertEquals(4, (int) list.get(1));
    assertEquals(5, (int) list.get(2));
    assertEquals(2, (int) list.get(3));
    assertEquals(3, (int) list.get(4));
  }

  @Test
  public void removeAllTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    list.removeAll(Arrays.asList(5, 3, 1));
    assertEquals(2, (int) list.get(0));
    assertEquals(4, (int) list.get(1));
  }

  @Test
  public void retainAllTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    list.retainAll(Arrays.asList(2, 10, 15));
    assertEquals(1, list.size());
    assertEquals(2, (int) list.get(0));
  }

  @Test
  public void sortTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
    list.sort(Integer::compareTo);
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
    assertEquals(4, (int) list.get(3));
    assertEquals(5, (int) list.get(4));
  }

  @Test
  public void clearTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    list.clear();
    assertEquals(0, list.size());
  }

  @Test
  public void getTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    assertEquals(4, (int) list.get(3));
  }

  @Test
  public void setTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    list.set(1, 7);
    assertEquals(3, list.size());
    assertEquals(1, (int) list.get(0));
    assertEquals(7, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
  }

  @Test
  public void addElementToIndexTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    list.add(1, 7);
    assertEquals(4, list.size());
    assertEquals(1, (int) list.get(0));
    assertEquals(7, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    assertEquals(3, (int) list.get(3));
  }

  @Test
  public void removeElementByIndexTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    list.remove(2);
    assertEquals(2, list.size());
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
  }

  @Test
  public void indexOfTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    assertEquals(1, list.indexOf(2));
    assertEquals(-1, list.indexOf(7));
  }

  @Test
  public void lastIndexOfTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3, 7, 3));
    assertEquals(4, list.lastIndexOf(3));
    assertEquals(-1, list.lastIndexOf(10));
  }

  @Test
  public void iteratorTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    Iterator<Integer> iterator = list.iterator();
    assertEquals(1, (int) iterator.next());
    assertEquals(2, (int) iterator.next());
    assertEquals(3, (int) iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void collectionsAddAllTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    Collections.addAll(list, 5, 6);
    assertEquals(5, list.size());
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
    assertEquals(5, (int) list.get(3));
    assertEquals(6, (int) list.get(4));
  }

  @Test
  public void collectionsCopyTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> copy = new ArrayList<>(Arrays.asList(5, 6, 7));
    Collections.copy(copy, list);
    assertEquals(3, copy.size());
    assertEquals(Arrays.asList(1, 2, 3), copy);
  }

  @Test
  public void collectionsSortTest() {
    List<Integer> list = new MyArrayList<>(Arrays.asList(3, 2, 1));
    Collections.sort(list, Integer::compareTo);
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
  }
}
