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

    @NotNull
    public static <A, B> Dual<A, B> valueOf(@Nullable A a, @Nullable B b) {
        return new Dual<>(a, b);
    }

    @NotNull
    public static <A, B> Dual<A, B> valueOf(@NotNull Map.Entry<A, B> entry) {
        return new Dual<>(entry.getKey(), entry.getValue());
    }

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

    public Dual() {
        this(null, null);
    }

    public Dual(@Nullable A left, @Nullable B right) {
        this.left = left;
        this.right = right;
    }

    public A getLeft() {
        return left;
    }

    public Object getLeftOrRight() {
        return getLeft() != null ? getLeft() : getRight();
    }

    public B getRight() {
        return right;
    }

    public Object getRightOrLeft() {
        return getRight() != null ? getRight() : getLeft();
    }

    public void setLeft(@Nullable A left) {
        this.left = left;
    }

    public void setRight(@Nullable B right) {
        this.right = right;
    }

    public boolean isEmpty() {
        return getLeft() == null && getRight() == null;
    }

    @NotNull
    public String join(@NotNull String delimiter) {
        return getLeft() + delimiter + getRight();
    }

    public <R> R map(@NotNull BiFunction<A, B, R> consumer) {
        return consumer.apply(getLeft(), getRight());
    }

    @NotNull
    public <L, R> Dual<L, R> map(@NotNull Function<A, L> leftFunction, @NotNull Function<B, R> rightFunction) {
        return new Dual<>(leftFunction.apply(getLeft()), rightFunction.apply(getRight()));
    }

    @NotNull
    public <L> Dual<L, B> mapLeft(@NotNull Function<A, L> leftFunction) {
        return new Dual<>(leftFunction.apply(getLeft()), getRight());
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return Objects.equals(getLeft(), o) || Objects.equals(getRight(), o);
        }

        return equals((Dual<?, ?>) o);
    }

    public boolean equals(@NotNull Dual<?, ?> dual) {
        return Objects.equals(getLeft(), dual.getLeft()) && Objects.equals(getRight(), dual.getRight());
    }

    @Override
    public int hashCode() {
        int result = getLeft() != null ? getLeft().hashCode() : 0;
        result = 31 * result + (getRight() != null ? getRight().hashCode() : 0);
        return result;
    }
}
