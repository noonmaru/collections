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

public interface Node<E>
{

    E getItem();

    E setItem(E item);

    Node<E> previous();

    Node<E> next();

    Node<E> linkBefore(E item);

    Node<E> linkAfter(E item);

    boolean isLinked();

    boolean unlink();

    void clear();

}
