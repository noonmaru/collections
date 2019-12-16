/*
 * Copyright (c) 2019 Noonmaru
 *
 * Licensed under the General Public License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/gpl-2.0.php
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.noonmaru.collections.mut;

import com.github.noonmaru.collections.Node;
import com.github.noonmaru.collections.NodeCollection;
import com.github.noonmaru.collections.NodeDeque;
import com.github.noonmaru.collections.NodeList;

import java.util.*;
import java.util.function.Consumer;

public class LinkedNodeList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, NodeCollection<E>, NodeList<E>, NodeDeque<E>, Cloneable, java.io.Serializable
{
    transient int size = 0;

    transient NodeImpl<E> first, last;

    public LinkedNodeList()
    {}

    public LinkedNodeList(Collection<? extends E> c)
    {
        addAll(c);
    }

    NodeImpl<E> linkFirst(E e)
    {
        final NodeImpl<E> f = first;
        final NodeImpl<E> newNode = new NodeImpl<>(this, null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
        modCount++;

        return newNode;
    }

    NodeImpl<E> linkLast(E e)
    {
        final NodeImpl<E> l = last;
        final NodeImpl<E> newNode = new NodeImpl<>(this, l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;

        return newNode;
    }

    NodeImpl<E> linkBefore(E e, NodeImpl<E> node)
    {
        final NodeImpl<E> prev = node.prev;
        final NodeImpl<E> newNode = new NodeImpl<>(this, prev, e, node);
        node.prev = newNode;
        if (prev == null)
            first = newNode;
        else
            prev.next = newNode;
        size++;
        modCount++;

        return newNode;
    }

    NodeImpl<E> linkAfter(E e, NodeImpl<E> node)
    {
        final NodeImpl<E> next = node.next;
        final NodeImpl<E> newNode = new NodeImpl<>(this, node, e, next);
        node.next = newNode;
        if (next == null)
            last = newNode;
        else
            next.prev = newNode;
        size++;
        modCount++;

        return newNode;
    }

    E unlinkFirst(NodeImpl<E> f)
    {
        // assert f == first && f != null;
        final E element = f.item;
        final NodeImpl<E> next = f.next;
        f.list = null;
        f.item = null;
        // f.next = null; // help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }

    E unlinkLast(NodeImpl<E> l)
    {
        // assert l == last && l != null;
        final E element = l.item;
        final NodeImpl<E> prev = l.prev;
        l.list = null;
        l.item = null;
        // l.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        modCount++;
        return element;
    }

    E unlink(NodeImpl<E> x)
    {
        // assert x != null;
        final E element = x.item;
        final NodeImpl<E> next = x.next;
        final NodeImpl<E> prev = x.prev;

        x.list = null;

        if (prev == null)
        {
            first = next;
        }
        else
        {
            prev.next = next;
            // x.prev = null;
        }

        if (next == null)
        {
            last = prev;
        }
        else
        {
            next.prev = prev;
            // x.next = null;
        }

        // x.item = null;
        size--;
        modCount++;
        return element;
    }

    public E getFirst()
    {
        final NodeImpl<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    public E getLast()
    {
        final NodeImpl<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }

    public E removeFirst()
    {
        final NodeImpl<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }

    public E removeLast()
    {
        final NodeImpl<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return unlinkLast(l);
    }

    public void addFirst(E e)
    {
        linkFirst(e);
    }

    public void addLast(E e)
    {
        linkLast(e);
    }

    public boolean contains(Object o)
    {
        return indexOf(o) != -1;
    }

    public int size()
    {
        return size;
    }

    public boolean add(E e)
    {
        linkLast(e);
        return true;
    }

    public boolean remove(Object o)
    {
        if (o == null)
        {
            for (NodeImpl<E> x = first; x != null; x = x.next)
            {
                if (x.item == null)
                {
                    unlink(x);
                    return true;
                }
            }
        }
        else
        {
            for (NodeImpl<E> x = first; x != null; x = x.next)
            {
                if (o.equals(x.item))
                {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addAll(Collection<? extends E> c)
    {
        return addAll(size, c);
    }

    public boolean addAll(int index, Collection<? extends E> c)
    {
        checkPositionIndex(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;

        NodeImpl<E> prev, succ;
        if (index == size)
        {
            succ = null;
            prev = last;
        }
        else
        {
            succ = node(index);
            prev = succ.prev;
        }

        for (Object o : a)
        {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            NodeImpl<E> newNode = new NodeImpl<>(this, prev, e, null);
            if (prev == null)
                first = newNode;
            else
                prev.next = newNode;
            prev = newNode;
        }

        if (succ == null)
        {
            last = prev;
        }
        else
        {
            prev.next = succ;
            succ.prev = prev;
        }

        size += numNew;
        modCount++;
        return true;
    }

    public void clear()
    {
        for (NodeImpl<E> x = first; x != null; )
        {
            NodeImpl<E> next = x.next;
            x.list = null;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
        modCount++;
    }

    public E get(int index)
    {
        checkElementIndex(index);
        return node(index).item;
    }

    public E set(int index, E element)
    {
        checkElementIndex(index);
        NodeImpl<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    public void add(int index, E element)
    {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    public E remove(int index)
    {
        checkElementIndex(index);
        return unlink(node(index));
    }

    private boolean isElementIndex(int index)
    {
        return index >= 0 && index < size;
    }

    private boolean isPositionIndex(int index)
    {
        return index >= 0 && index <= size;
    }

    private String outOfBoundsMsg(int index)
    {
        return "Index: " + index + ", Size: " + size;
    }

    private void checkElementIndex(int index)
    {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index)
    {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    NodeImpl<E> node(int index)
    {
        // assert isElementIndex(index);

        if (index < (size >> 1))
        {
            NodeImpl<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        }
        else
        {
            NodeImpl<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    public int indexOf(Object o)
    {
        int index = 0;
        if (o == null)
        {
            for (NodeImpl<E> x = first; x != null; x = x.next)
            {
                if (x.item == null)
                    return index;
                index++;
            }
        }
        else
        {
            for (NodeImpl<E> x = first; x != null; x = x.next)
            {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o)
    {
        int index = size;
        if (o == null)
        {
            for (NodeImpl<E> x = last; x != null; x = x.prev)
            {
                index--;
                if (x.item == null)
                    return index;
            }
        }
        else
        {
            for (NodeImpl<E> x = last; x != null; x = x.prev)
            {
                index--;
                if (o.equals(x.item))
                    return index;
            }
        }
        return -1;
    }

    public E peek()
    {
        final NodeImpl<E> f = first;
        return (f == null) ? null : f.item;
    }

    public E element()
    {
        return getFirst();
    }

    public E poll()
    {
        final NodeImpl<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    public E remove()
    {
        return removeFirst();
    }

    public boolean offer(E e)
    {
        return add(e);
    }

    public boolean offerFirst(E e)
    {
        addFirst(e);
        return true;
    }

    public boolean offerLast(E e)
    {
        addLast(e);
        return true;
    }

    public E peekFirst()
    {
        final NodeImpl<E> f = first;
        return (f == null) ? null : f.item;
    }

    public E peekLast()
    {
        final NodeImpl<E> l = last;
        return (l == null) ? null : l.item;
    }

    public E pollFirst()
    {
        final NodeImpl<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    public E pollLast()
    {
        final NodeImpl<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }

    public void push(E e)
    {
        addFirst(e);
    }

    public E pop()
    {
        return removeFirst();
    }

    public boolean removeFirstOccurrence(Object o)
    {
        return remove(o);
    }

    public boolean removeLastOccurrence(Object o)
    {
        if (o == null)
        {
            for (NodeImpl<E> x = last; x != null; x = x.prev)
            {
                if (x.item == null)
                {
                    unlink(x);
                    return true;
                }
            }
        }
        else
        {
            for (NodeImpl<E> x = last; x != null; x = x.prev)
            {
                if (o.equals(x.item))
                {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    public ListIterator<E> listIterator(int index)
    {
        checkPositionIndex(index);
        return new ListItr(index);
    }

    public Iterator<E> descendingIterator()
    {
        return new DescendingIterator();
    }

    @Override
    public Node<E> getNode(Object o)
    {
        if (o == null)
        {
            for (NodeImpl<E> x = first; x != null; x = x.next)
            {
                if (x.item == null)
                    return x;
            }
        }
        else
        {
            for (NodeImpl<E> x = first; x != null; x = x.next)
            {
                if (o.equals(x.item))
                    return x;
            }
        }
        return null;
    }

    @Override
    public Node<E> getNode(int index)
    {
        checkElementIndex(index);
        return node(index);
    }

    @Override
    public Node<E> addNode(E e)
    {
        return linkLast(e);
    }

    @Override
    public Node<E> addNode(int index, E e)
    {
        checkPositionIndex(index);

        if (index == size)
            return linkLast(e);
        else
            return linkBefore(e, node(index));
    }

    @Override
    public Node<E> addFirstNode(E e)
    {
        return linkFirst(e);
    }

    @Override
    public Node<E> addLastNode(E e)
    {
        return linkLast(e);
    }

    @Override
    public Node<E> offerNode(E e)
    {
        return linkLast(e);
    }

    @Override
    public Node<E> offerFirstNode(E e)
    {
        return linkFirst(e);
    }

    @Override
    public Node<E> offerLastNode(E e)
    {
        return linkLast(e);
    }

    @Override
    public Node<E> getFirstNode()
    {
        final NodeImpl<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f;
    }

    @Override
    public Node<E> getLastNode()
    {
        final NodeImpl<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l;
    }

    @Override
    public Node<E> peekNode()
    {
        return this.first;
    }

    @Override
    public Node<E> peekFirstNode()
    {
        return this.first;
    }

    @Override
    public Node<E> peekLastNode()
    {
        return this.last;
    }

    @SuppressWarnings("unchecked")
    private LinkedNodeList<E> superClone()
    {
        try
        {
            return (LinkedNodeList<E>) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(e);
        }
    }

    public Object clone()
    {
        LinkedNodeList<E> clone = superClone();

        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;

        for (NodeImpl<E> x = first; x != null; x = x.next)
            clone.add(x.item);

        return clone;
    }

    public Object[] toArray()
    {
        Object[] result = new Object[size];
        int i = 0;
        for (NodeImpl<E> x = first; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a)
    {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (NodeImpl<E> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException
    {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out size
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (NodeImpl<E> x = first; x != null; x = x.next)
            s.writeObject(x.item);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException
    {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // Read in size
        int size = s.readInt();

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++)
            linkLast((E) s.readObject());
    }

    @Override
    public Spliterator<E> spliterator()
    {
        return new LLSpliterator<E>(this, -1, 0);
    }

    static final class LLSpliterator<E> implements Spliterator<E>
    {
        static final int BATCH_UNIT = 1 << 10; // batch array size increment

        static final int MAX_BATCH = 1 << 25; // max batch array size;

        final LinkedNodeList<E> list; // null OK unless traversed

        NodeImpl<E> current; // current node; null until initialized

        int est; // size estimate; -1 until first needed

        int expectedModCount; // initialized when est set

        int batch; // batch size for splits

        LLSpliterator(LinkedNodeList<E> list, int est, int expectedModCount)
        {
            this.list = list;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEst()
        {
            int s; // force initialization
            final LinkedNodeList<E> lst;
            if ((s = est) < 0)
            {
                if ((lst = list) == null)
                    s = est = 0;
                else
                {
                    expectedModCount = lst.modCount;
                    current = lst.first;
                    s = est = lst.size;
                }
            }
            return s;
        }

        public long estimateSize()
        {
            return getEst();
        }

        public Spliterator<E> trySplit()
        {
            NodeImpl<E> p;
            int s = getEst();
            if (s > 1 && (p = current) != null)
            {
                int n = batch + BATCH_UNIT;
                if (n > s)
                    n = s;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do
                {
                    a[j++] = p.item;
                }
                while ((p = p.next) != null && j < n);
                current = p;
                batch = j;
                est = s - j;
                return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> action)
        {
            NodeImpl<E> p;
            int n;
            if (action == null)
                throw new NullPointerException();
            if ((n = getEst()) > 0 && (p = current) != null)
            {
                current = null;
                est = 0;
                do
                {
                    E e = p.item;
                    p = p.next;
                    action.accept(e);
                }
                while (p != null && --n > 0);
            }
            if (list.modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super E> action)
        {
            NodeImpl<E> p;
            if (action == null)
                throw new NullPointerException();
            if (getEst() > 0 && (p = current) != null)
            {
                --est;
                E e = p.item;
                current = p.next;
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public int characteristics()
        {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    static class NodeImpl<E> implements Node<E>
    {
        LinkedNodeList<E> list;

        NodeImpl<E> prev, next;

        E item;

        NodeImpl(LinkedNodeList<E> list, NodeImpl<E> prev, E item, NodeImpl<E> next)
        {
            this.list = list;
            this.prev = prev;
            this.next = next;
            this.item = item;
        }

        @Override
        public NodeImpl<E> previous()
        {
            if (this.list != null)
                return this.prev;

            NodeImpl<E> p = this.prev;

            if (p != null)
            {

                this.item = null;
                this.prev = this.next = null;

                if (p != null && p.list == null)
                {
                    do
                    {
                        p = p.prev;
                    }
                    while (p != null && p.list == null);
                }
            }

            return p;
        }

        @Override
        public NodeImpl<E> next()
        {
            if (this.list != null)
                return this.next;

            NodeImpl<E> n = this.next;

            this.item = null;
            this.prev = this.next = null;

            if (n != null && n.list == null)
            {
                do
                {
                    n = n.next;
                }
                while (n != null && n.list == null);
            }

            return n;
        }

        @Override
        public NodeImpl<E> linkBefore(E item)
        {
            LinkedNodeList<E> list = this.list;

            if (list == null)
                throw new IllegalArgumentException("Unlinked node");

            return list.linkBefore(item, this);
        }

        @Override
        public NodeImpl<E> linkAfter(E item)
        {
            LinkedNodeList<E> list = this.list;

            if (list == null)
                throw new IllegalArgumentException("Unlinked node");

            return list.linkAfter(item, this);
        }

        @Override
        public E getItem()
        {
            return this.item;
        }

        @Override
        public E setItem(E item)
        {
            E oldItem = this.item;
            this.item = item;

            return oldItem;
        }

        @Override
        public boolean isLinked()
        {
            return this.list != null;
        }

        @Override
        public boolean unlink()
        {
            LinkedNodeList<E> list = this.list;

            if (list == null)
                return false;

            list.unlink(this);
            return true;
        }

        @Override
        public void clear()
        {
            unlink();
            this.item = null;
            this.prev = this.next = null;
        }

        @Override
        public String toString()
        {
            return String.valueOf(this.item);
        }
    }

    private class ListItr implements ListIterator<E>
    {
        private NodeImpl<E> lastReturned;

        private NodeImpl<E> next;

        private int nextIndex;

        private int expectedModCount = modCount;

        ListItr(int index)
        {
            // assert isPositionIndex(index);
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        public boolean hasNext()
        {
            return nextIndex < size;
        }

        public E next()
        {
            checkForComodification();
            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        public boolean hasPrevious()
        {
            return nextIndex > 0;
        }

        public E previous()
        {
            checkForComodification();
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next = (next == null) ? last : next.prev;
            nextIndex--;
            return lastReturned.item;
        }

        public int nextIndex()
        {
            return nextIndex;
        }

        public int previousIndex()
        {
            return nextIndex - 1;
        }

        public void remove()
        {
            checkForComodification();
            if (lastReturned == null)
                throw new IllegalStateException();

            NodeImpl<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        public void set(E e)
        {
            if (lastReturned == null)
                throw new IllegalStateException();
            checkForComodification();
            lastReturned.item = e;
        }

        public void add(E e)
        {
            checkForComodification();
            lastReturned = null;
            if (next == null)
                linkLast(e);
            else
                linkBefore(e, next);
            nextIndex++;
            expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super E> action)
        {
            Objects.requireNonNull(action);
            while (modCount == expectedModCount && nextIndex < size)
            {
                action.accept(next.item);
                lastReturned = next;
                next = next.next;
                nextIndex++;
            }
            checkForComodification();
        }

        final void checkForComodification()
        {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class DescendingIterator implements Iterator<E>
    {
        private final ListItr itr = new ListItr(size());

        public boolean hasNext()
        {
            return itr.hasPrevious();
        }

        public E next()
        {
            return itr.previous();
        }

        public void remove()
        {
            itr.remove();
        }
    }
}