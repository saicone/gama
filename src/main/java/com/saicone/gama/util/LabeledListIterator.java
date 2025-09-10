package com.saicone.gama.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * An implementation of the {@link ListIterator} interface compatible with labeled lists.<br>
 * Unlike a standard {@link ListIterator}, this iterator requires a label to be specified
 * when using the {@link #set(Object)} and {@link #add(Object)} methods.
 *
 * @author Rubenicos
 *
 * @param <L> the type of labels returned by this list iterator
 * @param <E> the type of elements returned by this list iterator
 */
public class LabeledListIterator<L, E> implements ListIterator<E> {

    private final ListIterator<Map.Entry<L, E>> iterator;

    /**
     * Constructs a new labeled list iterator that wraps the specified list iterator.
     *
     * @param iterator the list iterator to wrap
     */
    public LabeledListIterator(@NotNull ListIterator<Map.Entry<L, E>> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next().getValue();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public E previous() {
        return iterator.previous().getValue();
    }

    @Override
    public int nextIndex() {
        return iterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return iterator.previousIndex();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void set(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces the last element returned by {@link #next()} or {@link #previous()}
     * with the specified element and label.
     *
     * @param label the label to associate with the element
     * @param e     the element to set
     */
    public void set(@NotNull L label, E e) {
        iterator.set(new AbstractMap.SimpleEntry<>(label, e));
    }

    @Override
    public void add(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element into the list at the current position
     * and associates it with the given label.
     *
     * @param label the label to associate with the element
     * @param e     the element to add
     */
    public void add(@NotNull L label, E e) {
        iterator.set(new AbstractMap.SimpleEntry<>(label, e));
    }
}
