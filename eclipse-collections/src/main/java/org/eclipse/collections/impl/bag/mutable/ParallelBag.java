package org.eclipse.collections.impl.bag.mutable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.bag.ParallelBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.predicate.primitive.IntPredicate;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.partition.bag.PartitionMutableBag;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.parallel.ParallelIterableImpl;

/**
 * Une implémentation parallèle de MutableBag qui utilise le pattern Decorator.
 * Toutes les opérations sont exécutées en parallèle à l'aide d'un ExecutorService.
 */
public class ParallelBagImpl<T>
        extends BagDecorator<T>
        implements ParallelBag<T>
{
    private static final long serialVersionUID = 1L;

    private final ExecutorService executorService;
    private final int batchSize;

    public ParallelBagImpl(MutableBag<T> delegate, ExecutorService executorService, int batchSize)
    {
        super(delegate);
        this.executorService = executorService;
        this.batchSize = batchSize;
    }

    @Override
    public MutableBag<T> asSynchronized()
    {
        return new SynchronizedBag<>(this);
    }

    @Override
    public MutableBag<T> asUnmodifiable()
    {
        return new UnmodifiableBag<>(this);
    }

    @Override
    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
        new ParallelIterableImpl<>(this.delegate, this.executorService, this.batchSize)
                .forEach(procedure);
    }

    @Override
    public int occurrencesOf(Object item)
    {
        return this.delegate.occurrencesOf(item);
    }

    @Override
    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        return this.delegate.toMapOfItemToCount();
    }

    @Override
    public MutableBag<T> selectByOccurrences(IntPredicate predicate)
    {
        return new ParallelIterableImpl<>(this.delegate, this.executorService, this.batchSize)
                .select(predicate);
    }

    @Override
    public MutableSet<T> selectUnique()
    {
        return new ParallelIterableImpl<>(this.delegate, this.executorService, this.batchSize)
                .toSet();
    }

    @Override
    public boolean addOccurrences(T item, int occurrences)
    {
        return this.delegate.addOccurrences(item, occurrences);
    }

    @Override
    public boolean removeOccurrences(Object item, int occurrences)
    {
        return this.delegate.removeOccurrences(item, occurrences);
    }

    @Override
    public boolean setOccurrences(T item, int count)
    {
        return this.delegate.setOccurrences(item, count);
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        return this.delegate.newEmpty();
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.delegate.iterator();
    }

    @Override
    public int size()
    {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return this.delegate.contains(o);
    }

    @Override
    public Object[] toArray()
    {
        return this.delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return this.delegate.toArray(a);
    }

    @Override
    public boolean add(T t)
    {
        return this.delegate.add(t);
    }

    @Override
    public boolean remove(Object o)
    {
        return this.delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return this.delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        return this.delegate.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return this.delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return this.delegate.retainAll(c);
    }

    @Override
    public void clear()
    {
        this.delegate.clear();
    }

    @Override
    public Set<T> toSet()
    {
        return this.delegate.toSet();
    }

    @Override
    public MutableBag<T> with(T element)
    {
        return this.delegate.with(element);
    }

    @Override
    public MutableBag<T> without(T element)
    {
        return this.delegate.without(element);
    }

    @Override
    public MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        return this.delegate.withAll(elements);
    }

    @Override
    public MutableBag<T> withoutAll(Iterable<? extends T> elements)
    {
        return this.delegate.withoutAll(elements);
    }

    @Override
    public MutableBag<T> withOccurrences(T element, int occurrences)
    {
        return this.delegate.withOccurrences(element, occurrences);
    }

    @Override
    public MutableBag<T> withoutOccurrences(T element, int occurrences)
    {
        return this.delegate.withoutOccurrences(element, occurrences);
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.executorService;
    }

    @Override
    public int getBatchSize()
    {
        return this.batchSize;
    }

    @Override
    public MutableBag<T> getUnderlyingBag()
    {
        return this.delegate;
    }
} 