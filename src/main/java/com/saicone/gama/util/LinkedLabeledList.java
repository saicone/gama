package com.saicone.gama.util;

import com.saicone.gama.annotation.SoftOverride;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;

/**
 * Labeled list implementation backed by a {@link LinkedList}.<br>
 * As specified in the {@link LabeledList} interface, most of the methods that modify
 * the list without specifying a label will fail with an {@link UnsupportedOperationException}.
 *
 * @author Rubenicos
 *
 * @param <L> the type of labels in this list
 * @param <E> the type of elements in this list
 */
public class LinkedLabeledList<L, E> extends AbstractSequentialList<E> implements LabeledList<L, E>, LabeledDeque<L, E> {

    private final LinkedList<Map.Entry<L, E>> list;

    /**
     * Constructs an empty list.
     */
    public LinkedLabeledList() {
        this(new LinkedList<>());
    }

    /**
     * Constructs a list containing the elements of the specified collection,
     * in the order they are returned by the collection's iterator.
     *
     * @param collection the collection whose elements are to be placed into this list
     */
    @SuppressWarnings("unchecked")
    public LinkedLabeledList(Collection<? extends Map.Entry<? extends L, ? extends E>> collection) {
        this((LinkedList<Map.Entry<L, E>>) new LinkedList<>(collection));
    }

    /**
     * Constructs a list backed by the specified linked list.
     *
     * @param list the linked list to back this labeled list
     */
    public LinkedLabeledList(@NotNull LinkedList<Map.Entry<L, E>> list) {
        this.list = list;
    }

    @Override
    public E getFirst() {
        return list.getFirst().getValue();
    }

    @Override
    public E getLast() {
        return list.getLast().getValue();
    }

    @Override
    public E removeFirst() {
        return list.removeFirst().getValue();
    }

    @Override
    public E removeLast() {
        return list.removeLast().getValue();
    }

    @Override
    public void addFirst(E e) {
        LabeledList.super.addFirst(e);
    }

    @Override
    public void addFirst(@NotNull L label, E e) {
        list.addFirst(entry(label, e));
    }

    @Override
    public void addLast(E e) {
        LabeledList.super.addLast(e);
    }

    @Override
    public void addLast(@NotNull L label, E e) {
        list.addLast(entry(label, e));
    }

    @Override
    public void addBefore(@NotNull L existingLabel, @NotNull L label, E e) {
        final ListIterator<Map.Entry<L, E>> iterator = list.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey().equals(existingLabel)) {
                iterator.previous();
                iterator.add(entry(label, e));
                return;
            }
        }
        throw new NoSuchElementException("Label not found: " + existingLabel);
    }

    @Override
    public void addAfter(@NotNull L existingLabel, @NotNull L label, E e) {
        final ListIterator<Map.Entry<L, E>> iterator = list.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey().equals(existingLabel)) {
                iterator.add(entry(label, e));
                return;
            }
        }
        throw new NoSuchElementException("Label not found: " + existingLabel);
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Map.Entry<?,?>) {
            return list.contains(o);
        } else {
            for (Map.Entry<L, E> entry : list) {
                if (entry.getValue().equals(o) && o.equals(entry.getValue())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean contains(@NotNull L label, Object o) {
        return list.contains(entry(label, o));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o instanceof Map.Entry<?,?>) {
            return list.remove(o);
        } else {
            try {
                return removeLabel((L) o);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public @NotNull L getLabel(int index) {
        return list.get(index).getKey();
    }

    @Override
    public boolean removeLabel(@NotNull L label) {
        return list.removeIf(entry -> entry.getKey().equals(label));
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(int index) {
        return list.get(index).getValue();
    }

    @Override
    public E getValue(@NotNull L label) {
        for (Map.Entry<L, E> entry : list) {
            if (entry.getKey().equals(label)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public E set(int index, @NotNull L label, E element) {
        return list.set(index, entry(label, element)).getValue();
    }

    @Override
    public void add(int index, @NotNull L label, E element) {
        list.add(index, entry(label, element));
    }

    @Override
    public E remove(int index) {
        return list.remove(index).getValue();
    }

    @Override
    public int indexOf(Object o) {
        if (o instanceof Map.Entry<?,?>) {
            return list.indexOf(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int indexOf(@NotNull L label, Object o) {
        return list.indexOf(entry(label, o));
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o instanceof Map.Entry<?,?>) {
            return list.lastIndexOf(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int lastIndexOf(@NotNull L label, Object o) {
        return list.lastIndexOf(entry(label, o));
    }

    @Override
    public @NotNull List<L> labels() {
        final List<L> list = new ArrayList<>();
        for (Map.Entry<L, E> entry : this.list) {
            list.add(entry.getKey());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public E peek() {
        final Map.Entry<L, E> entry = list.peek();
        return entry == null ? null : entry.getValue();
    }

    @Override
    public E element() {
        return list.element().getValue();
    }

    @Override
    public E poll() {
        final Map.Entry<L, E> entry = list.poll();
        return entry == null ? null : entry.getValue();
    }

    @Override
    public E remove() {
        return list.remove().getValue();
    }

    @Override
    public boolean offer(@NotNull L label, E e) {
        return list.offer(entry(label, e));
    }

    @Override
    public boolean offerFirst(@NotNull L label, E e) {
        return list.offerFirst(entry(label, e));
    }

    @Override
    public boolean offerLast(@NotNull L label, E e) {
        return list.offerLast(entry(label, e));
    }

    @Override
    public E peekFirst() {
        final Map.Entry<L, E> entry = list.peekFirst();
        return entry == null ? null : entry.getValue();
    }

    @Override
    public E peekLast() {
        final Map.Entry<L, E> entry = list.peekLast();
        return entry == null ? null : entry.getValue();
    }

    @Override
    public E pollFirst() {
        final Map.Entry<L, E> entry = list.pollFirst();
        return entry == null ? null : entry.getValue();
    }

    @Override
    public E pollLast() {
        final Map.Entry<L, E> entry = list.pollLast();
        return entry == null ? null : entry.getValue();
    }

    @Override
    public void push(@NotNull L label, E e) {
        list.push(entry(label, e));
    }

    @Override
    public E pop() {
        return list.pop().getValue();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o instanceof Map.Entry<?,?>) {
            return list.removeFirstOccurrence(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o instanceof Map.Entry<?,?>) {
            return list.removeLastOccurrence(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new LabeledListIterator<>(list.listIterator(index));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private final Iterator<Map.Entry<L, E>> iterator = list.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next().getValue();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        return new LinkedLabeledList<>((LinkedList<Map.Entry<L, E>>) list.clone());
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (Map.Entry<L, E> entry : list) {
            result[i++] = entry.getValue();
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size())
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size());
        int i = 0;
        Object[] result = a;
        for (Map.Entry<L, E> entry : list) {
            result[i++] = entry.getValue();
        }
        if (a.length > size()) {
            a[size()] = null;
        }
        return a;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new LabeledSpliterator<>(list.spliterator());
    }

    @SoftOverride
    public LinkedList<E> reversed() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    private static <K, V> Map.Entry<K, V> entry(K k, V v) {
        return new AbstractMap.SimpleEntry<>(k, v);
    }
}
