package org.eclipse.collections.impl.bag.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.bag.SortedBag;
import org.eclipse.collections.api.factory.BagFactory;
import org.eclipse.collections.impl.bag.mutable.primitive.BooleanHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.ByteHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.CharHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.DoubleHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.FloatHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.IntHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.LongHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.ShortHashBag;

/**
 * Un constructeur pour créer des Bag avec différentes caractéristiques.
 */
public final class BagBuilder<T>
{
    private final MutableBag<T> bag;
    private boolean unmodifiable;
    private boolean synchronized_;
    private boolean parallel;
    private ExecutorService executorService;
    private int batchSize;
    private Comparator<? super T> comparator;

    private BagBuilder(MutableBag<T> bag)
    {
        this.bag = bag;
    }

    public static <T> BagBuilder<T> newBuilder()
    {
        return new BagBuilder<>(new HashBag<>());
    }

    public static <T> BagBuilder<T> newBuilder(int initialCapacity)
    {
        return new BagBuilder<>(new HashBag<>(initialCapacity));
    }

    public static <T> BagBuilder<T> newBuilder(Iterable<? extends T> elements)
    {
        return new BagBuilder<>(new HashBag<>(elements));
    }

    public static <T> BagBuilder<T> newBuilder(Collection<? extends T> collection)
    {
        return new BagBuilder<>(new HashBag<>(collection));
    }

    public BagBuilder<T> with(T element)
    {
        this.bag.add(element);
        return this;
    }

    public BagBuilder<T> withAll(Iterable<? extends T> elements)
    {
        this.bag.addAll(elements);
        return this;
    }

    public BagBuilder<T> withOccurrences(T element, int occurrences)
    {
        this.bag.addOccurrences(element, occurrences);
        return this;
    }

    public BagBuilder<T> unmodifiable()
    {
        this.unmodifiable = true;
        return this;
    }

    public BagBuilder<T> synchronized_()
    {
        this.synchronized_ = true;
        return this;
    }

    public BagBuilder<T> parallel(ExecutorService executorService, int batchSize)
    {
        this.parallel = true;
        this.executorService = executorService;
        this.batchSize = batchSize;
        return this;
    }

    public BagBuilder<T> sorted(Comparator<? super T> comparator)
    {
        this.comparator = comparator;
        return this;
    }

    public MutableBag<T> build()
    {
        MutableBag<T> result = this.bag;

        if (this.comparator != null)
        {
            result = new SortedBagImpl<>(result, this.comparator);
        }

        if (this.parallel)
        {
            result = new ParallelBagImpl<>(result, this.executorService, this.batchSize);
        }

        if (this.synchronized_)
        {
            result = new SynchronizedBag<>(result);
        }

        if (this.unmodifiable)
        {
            result = new UnmodifiableBag<>(result);
        }

        return result;
    }

    public static BagBuilder<Boolean> newBooleanBuilder()
    {
        return new BagBuilder<>(new BooleanHashBag());
    }

    public static BagBuilder<Byte> newByteBuilder()
    {
        return new BagBuilder<>(new ByteHashBag());
    }

    public static BagBuilder<Character> newCharBuilder()
    {
        return new BagBuilder<>(new CharHashBag());
    }

    public static BagBuilder<Double> newDoubleBuilder()
    {
        return new BagBuilder<>(new DoubleHashBag());
    }

    public static BagBuilder<Float> newFloatBuilder()
    {
        return new BagBuilder<>(new FloatHashBag());
    }

    public static BagBuilder<Integer> newIntBuilder()
    {
        return new BagBuilder<>(new IntHashBag());
    }

    public static BagBuilder<Long> newLongBuilder()
    {
        return new BagBuilder<>(new LongHashBag());
    }

    public static BagBuilder<Short> newShortBuilder()
    {
        return new BagBuilder<>(new ShortHashBag());
    }
} 