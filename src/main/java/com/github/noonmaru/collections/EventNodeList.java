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

package com.github.noonmaru.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;

public class EventNodeList<E> extends LinkedNodeList<E>
{

    private static final int HANDLER_LENGTH = EventType.values().length;

    @SuppressWarnings("unchecked")
    private final Handler<E>[] handlers = (Handler<E>[]) Array.newInstance(Handler.class, HANDLER_LENGTH);

    public EventNodeList()
    {}

    public EventNodeList(Collection<? extends E> c)
    {
        addAll(c);
    }

    public void registerListener(EventType eventType, NodeListener<E> listener)
    {
        Handler<E>[] handlers = this.handlers;
        int index = eventType.ordinal();
        Handler<E> handler = handlers[index];

        if (handler == null)
            handlers[index] = handler = new Handler<>();

        handler.register(listener);
    }

    private void callEvent(EventType type, NodeImpl<E> node, E item)
    {
        Handler<E> handler = this.handlers[type.ordinal()];

        if (handler != null)
        {
            for (NodeListener<E> listener : handler.getListeners())
            {
                try
                {
                    listener.onEvent(EventType.LINK, node, item);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private NodeImpl<E> onLink(NodeImpl<E> node, E item)
    {
        callEvent(EventType.LINK, node, item);

        return node;
    }

    @Override
    NodeImpl<E> linkFirst(E e)
    {
        return onLink(super.linkFirst(e), e);
    }

    @Override
    NodeImpl<E> linkLast(E e)
    {
        return onLink(super.linkLast(e), e);
    }

    @Override
    NodeImpl<E> linkBefore(E e, NodeImpl<E> node)
    {
        return onLink(super.linkBefore(e, node), e);
    }

    @Override
    NodeImpl<E> linkAfter(E e, NodeImpl<E> node)
    {
        return onLink(super.linkAfter(e, node), e);
    }

    private E onUnlink(NodeImpl<E> x, E item)
    {
        callEvent(EventType.UNLINK, x, item);

        return item;
    }

    @Override
    E unlink(NodeImpl<E> x)
    {
        return onUnlink(x, super.unlink(x));
    }

    @Override
    E unlinkFirst(NodeImpl<E> f)
    {
        return onUnlink(f, super.unlinkFirst(f));
    }

    @Override
    E unlinkLast(NodeImpl<E> l)
    {
        return onUnlink(l, super.unlinkLast(l));
    }

    public enum EventType
    {
        LINK, UNLINK
    }

    @FunctionalInterface
    public interface NodeListener<E>
    {
        void onEvent(EventType type, Node<E> node, E item);
    }

    private static class Handler<E>
    {
        private final LinkedList<NodeListener<E>> list = new LinkedList<>();

        private NodeListener<E>[] listeners;

        void register(NodeListener<E> listener)
        {
            this.list.add(listener);
            this.listeners = null;
        }

        @SuppressWarnings("unchecked")
        NodeListener<E>[] getListeners()
        {
            NodeListener<E>[] listeners = this.listeners;

            if (listeners == null)
            {
                LinkedList<NodeListener<E>> list = this.list;
                this.listeners = listeners = list.toArray((NodeListener<E>[]) Array.newInstance(NodeListener.class, list.size()));
            }

            return listeners;
        }
    }

}
