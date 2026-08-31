/*
 * MIT License.
 *
 * Copyright (c) 2025-2026 Rubenicos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.saicone.gama.util;

import org.jetbrains.annotations.NotNull;

import java.util.Deque;

/**
 * An extension of the {@link Deque} interface that associates a label for each element.<br>
 * Unlike {@link Deque}, this interface will fail with an {@link UnsupportedOperationException}
 * when using most of the methods that modify the deque without specifying a label.
 *
 * @author Rubenicos
 *
 * @param <L> the type of labels in this deque
 * @param <E> the type of elements in this deque
 */
public interface LabeledDeque<L, E> extends Deque<E> {

    @Override
    default boolean offer(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element into this deque with the given label.
     *
     * @param label the label to associate with the element
     * @param e     the element to add
     * @return      true if the element was added to this deque, false otherwise
     */
    boolean offer(@NotNull L label, E e);

    @Override
    default boolean offerFirst(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element at the front of this deque with the given label.
     *
     * @param label the label to associate with the element
     * @param e     the element to add
     * @return      true if the element was added to this deque, false otherwise
     */
    boolean offerFirst(@NotNull L label, E e);

    @Override
    default boolean offerLast(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element at the end of this deque with the given label.
     *
     * @param label the label to associate with the element
     * @param e     the element to add
     * @return      true if the element was added to this deque, false otherwise
     */
    boolean offerLast(@NotNull L label, E e);

    @Override
    default void push(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pushes an element onto the stack represented by this deque with the given label.
     *
     * @param label the label to associate with the element
     * @param e     the element to push
     */
    void push(@NotNull L label, E e);
}
