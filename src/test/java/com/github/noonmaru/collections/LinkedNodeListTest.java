package com.github.noonmaru.collections;

import com.github.noonmaru.collections.mut.LinkedNodeList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Nemo
 */
public class LinkedNodeListTest
{
    private static final String FIRST = "first", SECOND = "second", LAST = "last";

    @Test
    public void test()
    {
        LinkedNodeList<String> list = new LinkedNodeList<>();

        //test single element
        Node<String> first = list.addNode(FIRST);
        assertSame(first, list.getFirstNode());
        assertSame(first, list.getLastNode());
        assertEquals(FIRST, list.getFirst());
        assertEquals(FIRST, list.getLast());

        //test double element
        Node<String> last = list.addLastNode(LAST);
        assertSame(last, list.getLastNode());
        assertEquals(LAST, list.getLast());
        assertNotEquals(last, first);

        //test triple element
        Node<String> second = list.addNode(1, SECOND);
        assertSame(second, list.getNode(1));
        assertSame(second.previous(), first);
        assertSame(second.next(), last);
        assertSame(first.next(), second);
        assertSame(last.previous(), second);
        assertEquals(list.get(1), SECOND);

        assertNull(first.previous());
        assertNull(last.next());

        first.unlink();
        assertSame(first.next(), second);
        assertNull(first.next());//clear links
        assertSame(second, list.getFirstNode());
        assertSame(last, list.getLastNode());

        last.unlink();
        assertSame(last.previous(), second);
        assertNull(last.previous());
        assertNull(second.previous());
        assertNull(second.next());
        assertEquals(1, list.size());

        list.clear();
        assertNull(second.next());
        assertNull(second.previous());
    }
}
