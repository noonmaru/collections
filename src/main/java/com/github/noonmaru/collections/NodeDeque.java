package com.github.noonmaru.collections;

import java.util.Deque;

public interface NodeDeque<E> extends NodeQueue<E>, Deque<E>
{

    Node<E> addFirstNode(E e);

    Node<E> addLastNode(E e);

    Node<E> offerFirstNode(E e);

    Node<E> offerLastNode(E e);

    Node<E> getFirstNode();

    Node<E> getLastNode();

    Node<E> peekFirstNode();

    Node<E> peekLastNode();

}