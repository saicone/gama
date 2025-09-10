package com.saicone.gama.util;

import com.saicone.gama.annotation.SoftOverride;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An extension of the {@link List} interface that associates a label for each element.<br>
 * Unlike {@link List}, this interface will fail with an {@link UnsupportedOperationException}
 * when using most of the methods that modify the list without specifying a label.
 *
 * @author Rubenicos
 *
 * @param <L> the type of labels in this list
 * @param <E> the type of elements in this list
 */
public interface LabeledList<L, E> extends List<E> {

    @SoftOverride
    default void addFirst(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds the specified element at the beginning of the collection with the given label.
     *
     * @param label the label to associate with the element
     * @param e     the element to add
     */
    void addFirst(@NotNull L label, E e);

    @SoftOverride
    default void addLast(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds the specified element at the end of the collection with the given label.
     *
     * @param label the label to associate with the element
     * @param e     the element to add
     */
    void addLast(@NotNull L label, E e);

    /**
     * Adds the specified element with the given label before an existing label.
     *
     * @param existingLabel the label before which the new element will be added
     * @param label         the label to associate with the new element
     * @param e             the element to add
     */
    void addBefore(@NotNull L existingLabel, @NotNull L label, E e);

    /**
     * Adds the specified element with the given label after an existing label.
     *
     * @param existingLabel the label after which the new element will be added
     * @param label         the label to associate with the new element
     * @param e             the element to add
     */
    void addAfter(@NotNull L existingLabel, @NotNull L label, E e);

    @Override
    default boolean add(E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true if this list contains the specified element with the given label.
     *
     * @param label the label associated with the element
     * @param o     the element whose presence in this list is to be tested
     * @return      true if this list contains the specified element with the given label
     */
    boolean contains(@NotNull L label, Object o);

    @SoftOverride
    default E getFirst() {
        if (size() == 0) {
            throw new NoSuchElementException();
        } else {
            return get(0);
        }
    }

    @SoftOverride
    default E getLast() {
        int last = size() - 1;
        if (last < 0) {
            throw new NoSuchElementException();
        } else {
            return get(last);
        }
    }

    /**
     * Returns the element associated with the specified label.
     *
     * @param label the label whose associated element is to be returned
     * @return      the element associated with the specified label, null if not found
     */
    E getValue(@NotNull L label);

    @Override
    default E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces the element at the specified position in this list with the specified element
     * and associates it with the given label.
     *
     * @param index   the index of the element to replace
     * @param label   the label to associate with the new element
     * @param element the element to be stored at the specified position
     * @return        the element previously at the specified position
     */
    E set(int index, @NotNull L label, E element);

    @Override
    default void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element at the specified position in this list
     * and associates it with the given label.
     *
     * @param index   the index at which the specified element is to be inserted
     * @param label   the label to associate with the new element
     * @param element the element to be inserted
     */
    void add(int index, @NotNull L label, E element);

    /**
     * Returns the label associated with the element at the specified position in this list.
     *
     * @param index the index of the element whose label is to be returned
     * @return      the label associated with the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    @NotNull
    L getLabel(int index);

    /**
     * Removes the element associated with the specified label from this list, optional operation.
     *
     * @param label the label whose associated element is to be removed
     * @return      true if an element was removed
     */
    boolean removeLabel(@NotNull L label);

    @SoftOverride
    default E removeFirst() {
        if (size() == 0) {
            throw new NoSuchElementException();
        } else {
            return remove(0);
        }
    }

    @SoftOverride
    default E removeLast() {
        int last = size() - 1;
        if (last < 0) {
            throw new NoSuchElementException();
        } else {
            return remove(last);
        }
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * associated with the given label in this list, or -1 if this list does not contain such element.
     *
     * @param label the label associated with the element
     * @param o     the element to search for
     * @return      the index of the first occurrence of the specified element
     *              associated with the given label in this list, or -1 if not found
     */
    int indexOf(@NotNull L label, Object o);

    /**
     * Returns the index of the last occurrence of the specified element
     * associated with the given label in this list, or -1 if this list does not contain such element.
     *
     * @param label the label associated with the element
     * @param o     the element to search for
     * @return      the index of the last occurrence of the specified element
     *              associated with the given label in this list, or -1 if not found
     */
    int lastIndexOf(@NotNull L label, Object o);

    /**
     * Returns an unmodifiable view of the labels contained in this list, in proper sequence.
     *
     * @return an unmodifiable list of the labels contained in this list
     */
    @NotNull
    List<L> labels();

}
