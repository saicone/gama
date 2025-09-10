package com.saicone.gama.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Labeled list implementation backed by an {@link ArrayList}.<br>
 * As specified in the {@link LabeledList} interface, most of the methods that modify
 * the list without specifying a label will fail with an {@link UnsupportedOperationException}.
 *
 * @author Rubenicos
 *
 * @param <L> the type of labels in this list
 * @param <E> the type of elements in this list
 */
public class ArrayLabeledList<L, E> extends AbstractList<E> implements LabeledList<L, E> {

    private final ArrayList<Map.Entry<L, E>> list;

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is negative
     */
    public ArrayLabeledList(int initialCapacity) {
        this(new ArrayList<>(initialCapacity));
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ArrayLabeledList() {
        this(new ArrayList<>());
    }

    /**
     * Constructs a list containing the elements of the specified collection,
     * in the order they are returned by the collection's iterator.
     *
     * @param collection the collection whose elements are to be placed into this list
     */
    @SuppressWarnings("unchecked")
    public ArrayLabeledList(Collection<? extends Map.Entry<? extends L, ? extends E>> collection) {
        this((ArrayList<Map.Entry<L, E>>) new ArrayList<>(collection));
    }

    /**
     * Constructs a list backed by the specified list.
     *
     * @param list the list to back this list
     */
    public ArrayLabeledList(@NotNull ArrayList<Map.Entry<L, E>> list) {
        this.list = list;
    }

    /**
     * As specified on {@link ArrayList#trimToSize()}.
     */
    public void trimToSize() {
        list.trimToSize();
    }

    /**
     * As specified on {@link ArrayList#ensureCapacity(int)}.
     */
    public void ensureCapacity(int minCapacity) {
        list.ensureCapacity(minCapacity);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
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
    @SuppressWarnings("unchecked")
    public Object clone() {
        return new ArrayLabeledList<>((ArrayList<Map.Entry<L, E>>) list.clone());
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
    public void addFirst(@NotNull L label, E e) {
        list.add(0, entry(label, e));
    }

    @Override
    public void addLast(@NotNull L label, E e) {
        list.add(entry(label, e));
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
    public E remove(int index) {
        return list.remove(index).getValue();
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
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ArrayLabeledList)) return false;
        if (!super.equals(object)) return false;

        ArrayLabeledList<?, ?> that = (ArrayLabeledList<?, ?>) object;
        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + list.hashCode();
        return result;
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
    public void clear() {
        list.clear();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return super.retainAll(c);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new LabeledListIterator<>(list.listIterator(index));
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new ArrayLabeledList<>(list.subList(fromIndex, toIndex));
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        list.forEach(entry -> action.accept(entry.getValue()));
    }

    @Override
    public Spliterator<E> spliterator() {
        return new LabeledSpliterator<>(list.spliterator());
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return list.removeIf(entry -> filter.test(entry.getValue()));
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        list.replaceAll(entry -> {
            entry.setValue(operator.apply(entry.getValue()));
            return entry;
        });
    }

    @Override
    public void sort(Comparator<? super E> c) {
        list.sort((entry1, entry2) -> c.compare(entry1.getValue(), entry2.getValue()));
    }

    @NotNull
    private static <K, V> Map.Entry<K, V> entry(K k, V v) {
        return new AbstractMap.SimpleEntry<>(k, v);
    }
}
