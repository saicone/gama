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

    @NotNull
    public static <T> CyclicSupplier<T> valueOf(@NotNull List<T> objects, @NotNull Duration duration) {
        return new CyclicSupplier<>(objects, duration.toMillis());
    }

    @NotNull
    public static <T> CyclicSupplier<T> valueOf(@NotNull List<T> objects, long time, @NotNull TimeUnit unit) {
        return new CyclicSupplier<>(objects, unit.toMillis(time));
    }

    private final List<T> objects;
    private final long updateTime;

    protected CyclicSupplier(@NotNull List<T> objects, long updateTime) {
        this.objects = objects;
        this.updateTime = updateTime;
    }

    @Override
    public T get() {
        if (this.objects.isEmpty()) {
            return null;
        }
        return this.objects.get((int) ((System.currentTimeMillis() / this.updateTime) % this.objects.size()));
    }

    @NotNull
    public Optional<T> getOptional() {
        return Optional.ofNullable(get());
    }

    @NotNull
    public Duration getDuration() {
        return Duration.ofMillis(this.updateTime);
    }

    public long getTime(@NotNull TimeUnit unit) {
        return TimeUnit.MILLISECONDS.convert(this.updateTime, unit);
    }
}
