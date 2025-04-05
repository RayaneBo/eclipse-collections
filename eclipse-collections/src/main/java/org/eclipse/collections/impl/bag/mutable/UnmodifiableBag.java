/*
 * Copyright (c) 2024 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.bag.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

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
import org.eclipse.collections.api.factory.primitive.ObjectLongMaps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableObjectLongMap;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.partition.bag.PartitionMutableBag;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.collection.mutable.AbstractUnmodifiableMutableCollection;

/**
 * Une implémentation immuable de MutableBag qui utilise le pattern Decorator.
 * Toutes les opérations de modification lèvent une UnsupportedOperationException.
 */
public class UnmodifiableBag<T>
        extends BagDecorator<T>
        implements MutableBag<T>, Serializable
{
    private static final long serialVersionUID = 1L;

    public UnmodifiableBag(MutableBag<T> delegate)
    {
        super(delegate);
    }

    /**
     * This method will take a MutableBag and wrap it directly in a UnmodifiableBag.
     */
    public static <E, B extends MutableBag<E>> UnmodifiableBag<E> of(B bag)
    {
        if (bag == null)
        {
            throw new IllegalArgumentException("cannot create an UnmodifiableBag for null");
        }
        return new UnmodifiableBag<>(bag);
    }

    @Override
    public MutableBag<T> asUnmodifiable()
    {
        return this;
    }

    @Override
    public MutableBag<T> asSynchronized()
    {
        return new SynchronizedBag<>(this);
    }

    @Override
    public ImmutableBag<T> toImmutable()
    {
        return Bags.immutable.withAll(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.delegate.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public String toStringOfItemToCount()
    {
        return this.delegate.toStringOfItemToCount();
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        return this.delegate.newEmpty();
    }

    @Override
    public MutableBag<T> selectByOccurrences(IntPredicate predicate)
    {
        return this.delegate.selectByOccurrences(predicate);
    }

    @Override
    public MutableBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public MutableBag<T> select(Predicate<? super T> predicate)
    {
        return this.delegate.select(predicate);
    }

    @Override
    public <P> MutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.selectWith(predicate, parameter);
    }

    @Override
    public MutableBag<T> reject(Predicate<? super T> predicate)
    {
        return this.delegate.reject(predicate);
    }

    @Override
    public <P> MutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.rejectWith(predicate, parameter);
    }

    @Override
    public PartitionMutableBag<T> partition(Predicate<? super T> predicate)
    {
        return this.delegate.partition(predicate);
    }

    @Override
    public <P> PartitionMutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.partitionWith(predicate, parameter);
    }

    @Override
    public <S> MutableBag<S> selectInstancesOf(Class<S> clazz)
    {
        return this.delegate.selectInstancesOf(clazz);
    }

    @Override
    public <V> MutableBag<V> collect(Function<? super T, ? extends V> function)
    {
        return this.delegate.collect(function);
    }

    @Override
    public MutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.delegate.collectBoolean(booleanFunction);
    }

    @Override
    public MutableByteBag collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.delegate.collectByte(byteFunction);
    }

    @Override
    public MutableCharBag collectChar(CharFunction<? super T> charFunction)
    {
        return this.delegate.collectChar(charFunction);
    }

    @Override
    public MutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.delegate.collectDouble(doubleFunction);
    }

    @Override
    public MutableFloatBag collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.delegate.collectFloat(floatFunction);
    }

    @Override
    public MutableIntBag collectInt(IntFunction<? super T> intFunction)
    {
        return this.delegate.collectInt(intFunction);
    }

    @Override
    public MutableLongBag collectLong(LongFunction<? super T> longFunction)
    {
        return this.delegate.collectLong(longFunction);
    }

    @Override
    public MutableShortBag collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.delegate.collectShort(shortFunction);
    }

    @Override
    public <V> MutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.delegate.flatCollect(function);
    }

    @Override
    public MutableList<ObjectIntPair<T>> topOccurrences(int count)
    {
        return this.delegate.topOccurrences(count);
    }

    @Override
    public MutableList<ObjectIntPair<T>> bottomOccurrences(int count)
    {
        return this.delegate.bottomOccurrences(count);
    }

    @Override
    public <V> MutableBag<V> collectWithOccurrences(ObjectIntToObjectFunction<? super T, ? extends V> function)
    {
        return this.delegate.collectWithOccurrences(function, Bags.mutable.empty());
    }

    @Override
    public <P, A> MutableBag<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.delegate.collectWith(function, parameter);
    }

    @Override
    public <V> MutableBag<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.delegate.collectIf(predicate, function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.delegate.groupBy(function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.delegate.groupByEach(function);
    }

    @Override
    public int sizeDistinct()
    {
        return this.delegate.sizeDistinct();
    }

    @Override
    public int occurrencesOf(Object item)
    {
        return this.delegate.occurrencesOf(item);
    }

    @Override
    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.delegate.forEachWithOccurrences(objectIntProcedure);
    }

    @Override
    public boolean anySatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        return this.delegate.anySatisfyWithOccurrences(predicate);
    }

    @Override
    public boolean allSatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        return this.delegate.allSatisfyWithOccurrences(predicate);
    }

    @Override
    public boolean noneSatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        return this.delegate.noneSatisfyWithOccurrences(predicate);
    }

    @Override
    public T detectWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        return this.delegate.detectWithOccurrences(predicate);
    }

    /**
     * @since 9.1.
     */
    @Override
    public <V, R extends Collection<V>> R collectWithOccurrences(ObjectIntToObjectFunction<? super T, ? extends V> function, R target)
    {
        return this.delegate.collectWithOccurrences(function, target);
    }

    @Override
    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        return this.delegate.toMapOfItemToCount();
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    @Override
    public <S> MutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.delegate.zip(that);
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    @Override
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.delegate.zipWithIndex();
    }

    @Override
    public MutableBag<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public MutableBag<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public MutableBag<T> withOccurrences(T element, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public MutableBag<T> withoutOccurrences(T element, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public MutableBag<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    protected Object writeReplace()
    {
        return new UnmodifiableBagSerializationProxy<>(this.delegate);
    }

    private static class UnmodifiableBagSerializationProxy<T> implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        private MutableBag<T> delegate;

        public UnmodifiableBagSerializationProxy()
        {
            // Empty constructor for Externalizable class
        }

        private UnmodifiableBagSerializationProxy(MutableBag<T> bag)
        {
            this.delegate = bag;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException
        {
            try
            {
                out.writeObject(this.delegate);
            }
            catch (RuntimeException e)
            {
                if (e.getCause() instanceof IOException)
                {
                    throw (IOException) e.getCause();
                }
                throw e;
            }
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
        {
            this.delegate = (MutableBag<T>) in.readObject();
        }

        protected Object readResolve()
        {
            return this.delegate.asUnmodifiable();
        }
    }

    @Override
    public MutableSet<T> selectUnique()
    {
        return this.delegate.selectUnique();
    }

    @Override
    public <V> MutableObjectLongMap<V> sumByInt(Function<? super T, ? extends V> groupBy, IntFunction<? super T> function)
    {
        MutableObjectLongMap<V> result = ObjectLongMaps.mutable.empty();
        this.forEachWithOccurrences((each, occurrences) -> result.addToValue(
                groupBy.valueOf(each),
                function.intValueOf(each) * (long) occurrences));
        return result;
    }

    @Override
    public <V> MutableObjectLongMap<V> sumByLong(Function<? super T, ? extends V> groupBy, LongFunction<? super T> function)
    {
        MutableObjectLongMap<V> result = ObjectLongMaps.mutable.empty();
        this.forEachWithOccurrences((each, occurrences) -> result.addToValue(
                groupBy.valueOf(each),
                function.longValueOf(each) * (long) occurrences));
        return result;
    }

    @Override
    public RichIterable<T> distinctView()
    {
        return this.delegate.distinctView();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new UnmodifiableIterator<>(this.delegate.iterator());
    }

    @Override
    public boolean add(T t)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
    }

    private static class UnmodifiableIterator<T> implements Iterator<T>
    {
        private final Iterator<T> delegate;

        private UnmodifiableIterator(Iterator<T> delegate)
        {
            this.delegate = delegate;
        }

        @Override
        public boolean hasNext()
        {
            return this.delegate.hasNext();
        }

        @Override
        public T next()
        {
            return this.delegate.next();
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Cannot modify an unmodifiable bag");
        }
    }
}
