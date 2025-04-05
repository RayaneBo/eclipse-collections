/*
 * Copyright (c) 2022 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.bag.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.bag.primitive.MutableBooleanBag;
import org.eclipse.collections.api.bag.primitive.MutableByteBag;
import org.eclipse.collections.api.bag.primitive.MutableCharBag;
import org.eclipse.collections.api.bag.primitive.MutableDoubleBag;
import org.eclipse.collections.api.bag.primitive.MutableFloatBag;
import org.eclipse.collections.api.bag.primitive.MutableIntBag;
import org.eclipse.collections.api.bag.primitive.MutableLongBag;
import org.eclipse.collections.api.bag.primitive.MutableShortBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ObjectIntToObjectFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.predicate.primitive.IntPredicate;
import org.eclipse.collections.api.block.predicate.primitive.ObjectIntPredicate;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.partition.bag.PartitionMutableBag;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.SynchronizedRichIterable;
import org.eclipse.collections.impl.collection.mutable.AbstractSynchronizedMutableCollection;
import org.eclipse.collections.impl.collection.mutable.SynchronizedCollectionSerializationProxy;

/**
 * A synchronized view of a {@link MutableBag}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableBag#asSynchronized()
 * @since 1.0
 */
public class SynchronizedBag<T>
        extends AbstractSynchronizedMutableCollection<T>
        implements MutableBag<T>, Serializable
{
    private static final long serialVersionUID = 2L;

    private final Lock lock;

    public SynchronizedBag(MutableBag<T> bag)
    {
        this(bag, new ReentrantLock());
    }

    public SynchronizedBag(MutableBag<T> bag, Lock lock)
    {
        super(bag);
        this.lock = lock;
    }

    /**
     * This method will take a MutableBag and wrap it directly in a SynchronizedBag.
     */
    public static <E, B extends MutableBag<E>> SynchronizedBag<E> of(B bag)
    {
        return new SynchronizedBag<>(bag);
    }

    /**
     * This method will take a MutableBag and wrap it directly in a SynchronizedBag. Additionally,
     * a developer specifies which lock to use with the collection.
     */
    public static <E, B extends MutableBag<E>> SynchronizedBag<E> of(B bag, Lock lock)
    {
        return new SynchronizedBag<>(bag, lock);
    }

    @Override
    protected MutableBag<T> getDelegate()
    {
        return (MutableBag<T>) super.getDelegate();
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().newEmpty().asSynchronized();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    protected Object writeReplace()
    {
        return new SynchronizedCollectionSerializationProxy<>(this.getDelegate());
    }

    @Override
    public int addOccurrences(T item, int occurrences)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().addOccurrences(item, occurrences);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean removeOccurrences(Object item, int occurrences)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().removeOccurrences(item, occurrences);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean setOccurrences(T item, int occurrences)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().setOccurrences(item, occurrences);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().toMapOfItemToCount();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> selectByOccurrences(IntPredicate predicate)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().selectByOccurrences(predicate);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableList<ObjectIntPair<T>> topOccurrences(int count)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().topOccurrences(count);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableList<ObjectIntPair<T>> bottomOccurrences(int count)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().bottomOccurrences(count);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean anySatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().anySatisfyWithOccurrences(predicate);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean allSatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().allSatisfyWithOccurrences(predicate);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean noneSatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().noneSatisfyWithOccurrences(predicate);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public T detectWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().detectWithOccurrences(predicate);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.lock.lock();
        try
        {
            this.getDelegate().forEachWithOccurrences(objectIntProcedure);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public <V> MutableBag<V> collectWithOccurrences(ObjectIntToObjectFunction<? super T, ? extends V> function)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().collectWithOccurrences(function);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    /**
     * @since 9.1.
     */
    @Override
    public <V, R extends Collection<V>> R collectWithOccurrences(ObjectIntToObjectFunction<? super T, ? extends V> function, R target)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().collectWithOccurrences(function, target);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public int occurrencesOf(Object item)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().occurrencesOf(item);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public int sizeDistinct()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().sizeDistinct();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public String toStringOfItemToCount()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().toStringOfItemToCount();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> tap(Procedure<? super T> procedure)
    {
        return (MutableBag<T>) super.tap(procedure);
    }

    @Override
    public MutableBag<T> select(Predicate<? super T> predicate)
    {
        return (MutableBag<T>) super.select(predicate);
    }

    @Override
    public <P> MutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return (MutableBag<T>) super.selectWith(predicate, parameter);
    }

    @Override
    public MutableBag<T> reject(Predicate<? super T> predicate)
    {
        return (MutableBag<T>) super.reject(predicate);
    }

    @Override
    public <P> MutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return (MutableBag<T>) super.rejectWith(predicate, parameter);
    }

    @Override
    public PartitionMutableBag<T> partition(Predicate<? super T> predicate)
    {
        return (PartitionMutableBag<T>) super.partition(predicate);
    }

    @Override
    public <P> PartitionMutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return (PartitionMutableBag<T>) super.partitionWith(predicate, parameter);
    }

    @Override
    public MutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return (MutableBooleanBag) super.collectBoolean(booleanFunction);
    }

    @Override
    public MutableByteBag collectByte(ByteFunction<? super T> byteFunction)
    {
        return (MutableByteBag) super.collectByte(byteFunction);
    }

    @Override
    public MutableCharBag collectChar(CharFunction<? super T> charFunction)
    {
        return (MutableCharBag) super.collectChar(charFunction);
    }

    @Override
    public MutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return (MutableDoubleBag) super.collectDouble(doubleFunction);
    }

    @Override
    public MutableFloatBag collectFloat(FloatFunction<? super T> floatFunction)
    {
        return (MutableFloatBag) super.collectFloat(floatFunction);
    }

    @Override
    public MutableIntBag collectInt(IntFunction<? super T> intFunction)
    {
        return (MutableIntBag) super.collectInt(intFunction);
    }

    @Override
    public MutableLongBag collectLong(LongFunction<? super T> longFunction)
    {
        return (MutableLongBag) super.collectLong(longFunction);
    }

    @Override
    public MutableShortBag collectShort(ShortFunction<? super T> shortFunction)
    {
        return (MutableShortBag) super.collectShort(shortFunction);
    }

    @Override
    public <S> MutableBag<S> selectInstancesOf(Class<S> clazz)
    {
        return (MutableBag<S>) super.selectInstancesOf(clazz);
    }

    @Override
    public <V> MutableBag<V> collect(Function<? super T, ? extends V> function)
    {
        return (MutableBag<V>) super.<V>collect(function);
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Override
    @Deprecated
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return (MutableSet<Pair<T, Integer>>) super.zipWithIndex();
    }

    @Override
    public <P, V> MutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return (MutableBag<V>) super.collectWith(function, parameter);
    }

    @Override
    public <V> MutableBag<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return (MutableBag<V>) super.<V>collectIf(predicate, function);
    }

    @Override
    public <V> MutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return (MutableBag<V>) super.flatCollect(function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return (MutableBagMultimap<V, T>) super.<V>groupBy(function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return (MutableBagMultimap<V, T>) super.groupByEach(function);
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Override
    @Deprecated
    public <S> MutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return (MutableBag<Pair<T, S>>) super.zip(that);
    }

    @Override
    public MutableBag<T> asUnmodifiable()
    {
        return new UnmodifiableBag<>(this);
    }

    @Override
    public MutableBag<T> asSynchronized()
    {
        return this;
    }

    @Override
    public ImmutableBag<T> toImmutable()
    {
        return Bags.immutable.withAll(this);
    }

    @Override
    public MutableSet<T> selectUnique()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().selectUnique();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public RichIterable<T> distinctView()
    {
        this.lock.lock();
        try
        {
            return SynchronizedRichIterable.of(this.getDelegate().distinctView(), this.lock);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public Iterator<T> iterator()
    {
        return new SynchronizedIterator<>(this.getDelegate().iterator(), this.lock);
    }

    @Override
    public int size()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().size();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean isEmpty()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().isEmpty();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().contains(o);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public Object[] toArray()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().toArray();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().toArray(a);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean add(T t)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().add(t);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().remove(o);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().containsAll(c);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().addAll(c);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().removeAll(c);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().retainAll(c);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public void clear()
    {
        this.lock.lock();
        try
        {
            this.getDelegate().clear();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public Set<T> toSet()
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().toSet();
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> with(T element)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().with(element);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> without(T element)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().without(element);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().withAll(elements);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> withoutAll(Iterable<? extends T> elements)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().withoutAll(elements);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> withOccurrences(T element, int occurrences)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().withOccurrences(element, occurrences);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public MutableBag<T> withoutOccurrences(T element, int occurrences)
    {
        this.lock.lock();
        try
        {
            return this.getDelegate().withoutOccurrences(element, occurrences);
        }
        finally
        {
            this.lock.unlock();
        }
    }

    private static class SynchronizedIterator<T> implements Iterator<T>
    {
        private final Iterator<T> delegate;
        private final Lock lock;

        private SynchronizedIterator(Iterator<T> delegate, Lock lock)
        {
            this.delegate = delegate;
            this.lock = lock;
        }

        @Override
        public boolean hasNext()
        {
            this.lock.lock();
            try
            {
                return this.delegate.hasNext();
            }
            finally
            {
                this.lock.unlock();
            }
        }

        @Override
        public T next()
        {
            this.lock.lock();
            try
            {
                return this.delegate.next();
            }
            finally
            {
                this.lock.unlock();
            }
        }

        @Override
        public void remove()
        {
            this.lock.lock();
            try
            {
                this.delegate.remove();
            }
            finally
            {
                this.lock.unlock();
            }
        }
    }
}
