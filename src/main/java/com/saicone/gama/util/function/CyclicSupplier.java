package com.saicone.gama.util.function;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * An object that its only purpose is to serve an element from a List at defined duration.<br>
 * This class doesn't execute any task, every {@link CyclicSupplier#get()} call make a calculation
 * using current time in milliseconds.
 *
 * @author Rubenicos
 *
 * @param <T> the type of object that is being rotated,
 */
public class CyclicSupplier<T> implements Supplier<T> {

    /**
     * Create a cyclic supplier with the given list of objects and time to update.
     *
     * @param objects  the list of objects to rotate.
     * @param duration the duration to update the object.
     * @return         a newly generated cyclic supplier.
     * @param <T>      the type of object that is being rotated.
     */
    @NotNull
    public static <T> CyclicSupplier<T> valueOf(@NotNull List<T> objects, @NotNull Duration duration) {
        return new CyclicSupplier<>(objects, duration.toMillis());
    }

    /**
     * Create a cyclic supplier with the given list of objects and time to update.
     *
     * @param objects the list of objects to rotate.
     * @param time    the time to update the object.
     * @param unit    the time unit of the time parameter.
     * @return        a newly generated cyclic supplier.
     * @param <T>     the type of object that is being rotated.
     */
    @NotNull
    public static <T> CyclicSupplier<T> valueOf(@NotNull List<T> objects, long time, @NotNull TimeUnit unit) {
        return new CyclicSupplier<>(objects, unit.toMillis(time));
    }

    private final List<T> objects;
    private final long updateTime;

    private transient int lastIndex;

    /**
     * Constructs a cyclic supplier with the given parameters.
     *
     * @param objects    the list of objects to rotate.
     * @param updateTime the time in milliseconds to update the object.
     */
    protected CyclicSupplier(@NotNull List<T> objects, long updateTime) {
        this.objects = objects;
        this.updateTime = updateTime;
        this.lastIndex = getCurrentIndex();
    }

    /**
     * Get the current object based on the current time and the update time.
     *
     * @return the current object or null if the list is empty.
     * @throws IndexOutOfBoundsException if the current list is empty.
     */
    @Override
    public T get() {
        return this.objects.get((this.lastIndex = getCurrentIndex()));
    }

    /**
     * Get the current object based on the current time and the update time.<br>
     * If the current list is empty, an empty {@link Optional} is return.
     *
     * @return the current object as an {@link Optional}.
     */
    @NotNull
    public Optional<T> getOptional() {
        if (this.objects.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(get());
    }

    /**
     * Get the list of objects that are being rotated.
     *
     * @return a list of objects.
     */
    @NotNull
    public List<T> getObjects() {
        return objects;
    }

    /**
     * Get the update time as a {@link Duration} object.
     *
     * @return the update time as a {@link Duration} object.
     */
    @NotNull
    public Duration getDuration() {
        return Duration.ofMillis(this.updateTime);
    }

    /**
     * Get the update time in the specified time unit.
     *
     * @param unit the time unit to convert to.
     * @return     the update time in the specified time unit.
     */
    public long getTime(@NotNull TimeUnit unit) {
        return TimeUnit.MILLISECONDS.convert(this.updateTime, unit);
    }

    /**
     * Get the current index based on the current time and the update time.
     *
     * @return the current index.
     */
    public int getCurrentIndex() {
        return (int) ((System.currentTimeMillis() / this.updateTime) % this.objects.size());
    }

    /**
     * Get the last index that was served.
     *
     * @return the last index.
     */
    public int getLastIndex() {
        return lastIndex;
    }
}
