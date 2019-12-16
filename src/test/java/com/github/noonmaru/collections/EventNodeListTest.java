package com.github.noonmaru.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Nemo
 */
public class EventNodeListTest
{
    @Test
    public void test()
    {
        EventNodeList<String> list = new EventNodeList<>();

        Node<String> first = list.addNode("first");

        final String value = "second";

        list.registerListener(EventNodeList.EventType.LINK, (type, node, item) -> {
            assertEquals(item, value);
        });

        Node<String> second = list.addNode(value);

        list.registerListener(EventNodeList.EventType.UNLINK, (type, node, item) -> {
            assertEquals(item, "first");
        });

        first.unlink();
    }
}
