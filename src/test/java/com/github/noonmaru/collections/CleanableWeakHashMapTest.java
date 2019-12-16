package com.github.noonmaru.collections;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Nemo
 */
public class CleanableWeakHashMapTest
{
    @Test
    public void testCleaner() throws InterruptedException
    {
        Object key = new Object();
        Object value = new Object();

        CleanableWeakHashMap<Object, Object> map = new CleanableWeakHashMap<>(testValue -> {
            assertEquals(testValue, value);
        });

        map.put(key, value);
        System.gc();
        Thread.sleep(500L);
        assertFalse(map.isEmpty());
        key = null;
        System.gc();
        Thread.sleep(500L);
        assertTrue(map.isEmpty());
    }
}