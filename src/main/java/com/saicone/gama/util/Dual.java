package com.saicone.gama.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An object with the same functionality of a Pair object with mutable contents.<br>
 * This class provides utility methods to interact with its content.
 *
 * @author Rubenicos
 *
 * @param <A> the object type at left position.
 * @param <B> the object type at right position.
 */
public class Dual<A, B> {

    /**
     * Creates a new dual object with the given parameters.
     *
     * @param a the object at left position.
     * @param b the object at right position.
     * @return  a newly generated dual object.
     * @param <A> the object type at left position.
     * @param <B> the object type at right position.
     */
    @NotNull
    public static <A, B> Dual<A, B> valueOf(@Nullable A a, @Nullable B b) {
        return new Dual<>(a, b);
    }

    /**
     * Creates a new dual object from the given map entry.
     *
     * @param entry the map entry to create the dual object from.
     * @return      a newly generated dual object.
     * @param <A>   the object type at left position.
     * @param <B>   the object type at right position.
     */
    @NotNull
    public static <A, B> Dual<A, B> valueOf(@NotNull Map.Entry<A, B> entry) {
        return new Dual<>(entry.getKey(), entry.getValue());
    }

    /**
     * Creates a new dual object from the given array.
     * <ul>
     *     <li>If the array is null or empty, an empty dual object is returned.</li>
     *     <li>If the array has one element, a dual object with the first element and null is returned.</li>
     *     <li>If the array has two or more elements, a dual object with the first two elements is returned.</li>
     * </ul>
     *
     * @param array the array to create the dual object from.
     * @return      a newly generated dual object.
     * @param <T>   the object type at both positions.
     */
    @NotNull
    public static <T> Dual<T, T> valueOf(@Nullable T[] array) {
        if (array == null || array.length < 1) {
            return new Dual<>();
        } else if (array.length == 1) {
            return new Dual<>(array[0], null);
        } else {
            return new Dual<>(array[0], array[1]);
        }
    }

    /**
     * Creates a new dual object by applying the given functions to the given object.
     *
     * @param t the object to apply the functions to.
     * @param a the function to apply to get the left object.
     * @param b the function to apply to get the right object.
     * @return  a newly generated dual object.
     * @param <T> the type of the input object.
     * @param <A> the object type at left position.
     * @param <B> the object type at right position.
     */
    @NotNull
    public static <T, A, B> Dual<A, B> valueOf(@NotNull T t, @NotNull Function<T, A> a, @NotNull Function<T, B> b) {
        return new Dual<A, B>() {
            @Override
            public A getLeft() {
                return super.getLeft() != null ? super.getLeft() : a.apply(t);
            }

            @Override
            public B getRight() {
                return super.getRight() != null ? super.getRight() : b.apply(t);
            }
        };
    }

    private A left;
    private B right;

    /**
     * Constructs an empty dual object.
     */
    public Dual() {
        this(null, null);
    }

    /**
     * Constructs a dual object with the given parameters.
     *
     * @param left  the object at left position.
     * @param right the object at right position.
     */
    public Dual(@Nullable A left, @Nullable B right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Get the object at left position.
     *
     * @return the object at left position.
     */
    public A getLeft() {
        return left;
    }

    /**
     * Get the object at left position, or the object at right position if left is null.
     *
     * @return the object at left position if exists, otherwise the object at right position.
     */
    public Object getLeftOrRight() {
        return getLeft() != null ? getLeft() : getRight();
    }

    /**
     * Get the object at right position.
     *
     * @return the object at right position.
     */
    public B getRight() {
        return right;
    }

    /**
     * Get the object at right position, or the object at left position if right is null.
     *
     * @return the object at right position if exists, otherwise the object at left position.
     */
    public Object getRightOrLeft() {
        return getRight() != null ? getRight() : getLeft();
    }

    /**
     * Set the object at left position.
     *
     * @param left the object to set at left position.
     */
    public void setLeft(@Nullable A left) {
        this.left = left;
    }

    /**
     * Set the object at right position.
     *
     * @param right the object to set at right position.
     */
    public void setRight(@Nullable B right) {
        this.right = right;
    }

    /**
     * Check if the current object is empty.
     *
     * @return true if both left and right objects are null.
     */
    public boolean isEmpty() {
        return getLeft() == null && getRight() == null;
    }

    /**
     * Join the left and right objects as strings with the given delimiter.
     *
     * @param delimiter the delimiter to join the strings.
     * @return          a string with the left and right objects joined by the delimiter.
     */
    @NotNull
    public String join(@NotNull String delimiter) {
        return getLeft() + delimiter + getRight();
    }

    /**
     * Squash the current dual object into a single object using the given function.
     *
     * @param consumer the function to apply to the left and right objects.
     * @return         the result of the function applied to the left and right objects.
     * @param <R>      the type of the resulting object.
     */
    public <R> R map(@NotNull BiFunction<A, B, R> consumer) {
        return consumer.apply(getLeft(), getRight());
    }

    /**
     * Map the left and right objects to new objects using the given functions.
     *
     * @param leftFunction  the function to apply to the left object.
     * @param rightFunction the function to apply to the right object.
     * @return              a new dual object with the mapped left and right objects.
     * @param <L>           the type of the new left object.
     * @param <R>           the type of the new right object.
     */
    @NotNull
    public <L, R> Dual<L, R> map(@NotNull Function<A, L> leftFunction, @NotNull Function<B, R> rightFunction) {
        return new Dual<>(leftFunction.apply(getLeft()), rightFunction.apply(getRight()));
    }

    /**
     * Map only the left object to a new object using the given function.
     *
     * @param leftFunction the function to apply to the left object.
     * @return             a new dual object with the mapped left object and the original right object.
     * @param <L>          the type of the new left object.
     */
    @NotNull
    public <L> Dual<L, B> mapLeft(@NotNull Function<A, L> leftFunction) {
        return new Dual<>(leftFunction.apply(getLeft()), getRight());
    }

    /**
     * Map only the right object to a new object using the given function.
     *
     * @param rightFunction the function to apply to the right object.
     * @return              a new dual object with the original left object and the mapped right object.
     * @param <R>           the type of the new right object.
     */
    @NotNull
    public <R> Dual<A, R> mapRight(@NotNull Function<B, R> rightFunction) {
        return new Dual<>(getLeft(), rightFunction.apply(getRight()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Dual<A, B> clone() {
        try {
            return (Dual<A, B>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "Dual{" +
                "left=" + getLeft() +
                ", right=" + getRight() +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Dual)) return false;

        Dual<?, ?> dual = (Dual<?, ?>) o;
        return Objects.equals(getLeft(), dual.getLeft()) && Objects.equals(getRight(), dual.getRight());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getLeft());
        result = 31 * result + Objects.hashCode(getRight());
        return result;
    }
}
