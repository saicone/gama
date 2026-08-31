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

import java.util.Comparator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * An implementation of the {@link Spliterator} interface compatible with labeled collections.
 *
 * @author Rubenicos
 *
 * @param <L> the type of labels returned by this spliterator
 * @param <E> the type of elements returned by this spliterator
 */
public class LabeledSpliterator<L, E> implements Spliterator<E> {

    private final Spliterator<Map.Entry<L, E>> spliterator;

    /**
     * Constructs a new labeled spliterator that wraps the specified spliterator.
     *
     * @param spliterator the spliterator to wrap
     */
    public LabeledSpliterator(@NotNull Spliterator<Map.Entry<L, E>> spliterator) {
        this.spliterator = spliterator;
    }


    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
        return spliterator.tryAdvance((entry) -> action.accept(entry.getValue()));
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        spliterator.forEachRemaining((entry) -> action.accept(entry.getValue()));
    }

    @Override
    public Spliterator<E> trySplit() {
        return new LabeledSpliterator<>(spliterator.trySplit());
    }

    @Override
    public long estimateSize() {
        return spliterator.estimateSize();
    }

    @Override
    public long getExactSizeIfKnown() {
        return spliterator.getExactSizeIfKnown();
    }

    @Override
    public int characteristics() {
        return spliterator.characteristics();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return spliterator.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super E> getComparator() {
        throw new UnsupportedOperationException();
    }
}
