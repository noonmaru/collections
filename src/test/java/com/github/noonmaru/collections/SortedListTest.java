package com.github.noonmaru.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SortedListTest
{

    @Test
    public void add()
    {
        SortedList<Object> list = new SortedList<>();

        assertTrue(list.add(new Object()));
    }

    @Test
    public void sort()
    {
        SortedList<ModifiableObject> list = new SortedList<>();

        ModifiableObject one = new ModifiableObject(1);
        ModifiableObject two = new ModifiableObject(2);
        ModifiableObject three = new ModifiableObject(3);
        list.add(one);
        list.add(two);
        list.add(three);

        assertEquals(one, list.get(0));
        assertEquals(three, list.get(list.size() - 1));

        one.i += 4;
        three.i = 0;

        list.sort();

        assertEquals(three, list.get(0));
        assertEquals(one, list.get(list.size() - 1));
    }

    @Test
    public void binaryAdd()
    {
        SortedList<Integer> list = new SortedList<>();
        list.add(0);
        list.add(2);

        assertEquals(1, list.binaryAdd(1));
    }

    @Test
    public void binaryRemove()
    {

    }

    @Test
    public void addAll()
    {
    }

    @Test
    public void clear()
    {
    }

    @Test
    public void contains()
    {
    }

    @Test
    public void containsAll()
    {
    }

    @Test
    public void equals()
    {
    }

    @Test
    public void binarySearch()
    {
    }

    @Test
    public void get()
    {
    }

    @Test
    public void indexOf()
    {
    }

    @Test
    public void isEmpty()
    {
    }

    @Test
    public void lastIndexOf()
    {
    }

    @Test
    public void listIterator()
    {
    }

    @Test
    public void remove()
    {
    }

    @Test
    public void remove1()
    {
    }

    @Test
    public void removeAll()
    {
    }

    @Test
    public void retainAll()
    {
    }

    @Test
    public void size()
    {
    }

    @Test
    public void subList()
    {
    }

    @Test
    public void toArray()
    {
    }

    private static class ModifiableObject implements Comparable<ModifiableObject>
    {
        int i;

        public ModifiableObject(int i)
        {
            this.i = i;
        }

        @Override
        public int compareTo(ModifiableObject o)
        {
            return this.i - o.i;
        }
    }

}