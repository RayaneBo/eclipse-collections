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
 * Une fabrique pour créer différentes implémentations de Bag.
 */
public final class BagFactoryImpl implements BagFactory
{
    private static final BagFactoryImpl INSTANCE = new BagFactoryImpl();

    private BagFactoryImpl()
    {
    }

    public static BagFactoryImpl instance()
    {
        return INSTANCE;
    }

    @Override
    public <T> MutableBag<T> empty()
    {
        return new HashBag<>();
    }

    @Override
    public <T> MutableBag<T> of()
    {
        return this.empty();
    }

    @Override
    public <T> MutableBag<T> with()
    {
        return this.empty();
    }

    @Override
    public <T> MutableBag<T> of(T... elements)
    {
        return this.with(elements);
    }

    @Override
    public <T> MutableBag<T> with(T... elements)
    {
        return new HashBag<>(elements);
    }

    @Override
    public <T> MutableBag<T> ofAll(Iterable<? extends T> elements)
    {
        return this.withAll(elements);
    }

    @Override
    public <T> MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        return new HashBag<>(elements);
    }

    @Override
    public <T> MutableBag<T> ofInitialCapacity(int capacity)
    {
        return this.withInitialCapacity(capacity);
    }

    @Override
    public <T> MutableBag<T> withInitialCapacity(int capacity)
    {
        return new HashBag<>(capacity);
    }

    @Override
    public <T> MutableBag<T> ofAll(Collection<? extends T> collection)
    {
        return this.withAll(collection);
    }

    @Override
    public <T> MutableBag<T> withAll(Collection<? extends T> collection)
    {
        return new HashBag<>(collection);
    }

    @Override
    public <T> MutableBag<T> ofUnmodifiable()
    {
        return this.withUnmodifiable();
    }

    @Override
    public <T> MutableBag<T> withUnmodifiable()
    {
        return new UnmodifiableBag<>(new HashBag<>());
    }

    @Override
    public <T> MutableBag<T> ofSynchronized()
    {
        return this.withSynchronized();
    }

    @Override
    public <T> MutableBag<T> withSynchronized()
    {
        return new SynchronizedBag<>(new HashBag<>());
    }

    @Override
    public <T> MutableBag<T> ofParallel(ExecutorService executorService, int batchSize)
    {
        return this.withParallel(executorService, batchSize);
    }

    @Override
    public <T> MutableBag<T> withParallel(ExecutorService executorService, int batchSize)
    {
        return new ParallelBagImpl<>(new HashBag<>(), executorService, batchSize);
    }

    @Override
    public <T> SortedBag<T> ofSorted(Comparator<? super T> comparator)
    {
        return this.withSorted(comparator);
    }

    @Override
    public <T> SortedBag<T> withSorted(Comparator<? super T> comparator)
    {
        return new SortedBagImpl<>(new HashBag<>(), comparator);
    }

    @Override
    public MutableBag<Boolean> ofBoolean()
    {
        return this.withBoolean();
    }

    @Override
    public MutableBag<Boolean> withBoolean()
    {
        return new BooleanHashBag();
    }

    @Override
    public MutableBag<Byte> ofByte()
    {
        return this.withByte();
    }

    @Override
    public MutableBag<Byte> withByte()
    {
        return new ByteHashBag();
    }

    @Override
    public MutableBag<Character> ofChar()
    {
        return this.withChar();
    }

    @Override
    public MutableBag<Character> withChar()
    {
        return new CharHashBag();
    }

    @Override
    public MutableBag<Double> ofDouble()
    {
        return this.withDouble();
    }

    @Override
    public MutableBag<Double> withDouble()
    {
        return new DoubleHashBag();
    }

    @Override
    public MutableBag<Float> ofFloat()
    {
        return this.withFloat();
    }

    @Override
    public MutableBag<Float> withFloat()
    {
        return new FloatHashBag();
    }

    @Override
    public MutableBag<Integer> ofInt()
    {
        return this.withInt();
    }

    @Override
    public MutableBag<Integer> withInt()
    {
        return new IntHashBag();
    }

    @Override
    public MutableBag<Long> ofLong()
    {
        return this.withLong();
    }

    @Override
    public MutableBag<Long> withLong()
    {
        return new LongHashBag();
    }

    @Override
    public MutableBag<Short> ofShort()
    {
        return this.withShort();
    }

    @Override
    public MutableBag<Short> withShort()
    {
        return new ShortHashBag();
    }
} 